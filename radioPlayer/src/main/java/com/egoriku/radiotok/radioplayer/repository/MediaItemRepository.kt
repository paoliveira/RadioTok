package com.egoriku.radiotok.radioplayer.repository

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import androidx.core.net.toUri
import com.egoriku.mediaitemdsl.browsableMediaItem
import com.egoriku.mediaitemdsl.playableMediaItem
import com.egoriku.radiotok.common.datasource.*
import com.egoriku.radiotok.common.provider.IBitmapProvider
import com.egoriku.radiotok.common.provider.IStringResourceProvider
import com.egoriku.radiotok.db.RadioTokDb
import com.egoriku.radiotok.db.mapper.DbStationToModelMapper
import com.egoriku.radiotok.db.mapper.RadioEntityToModelMapper
import com.egoriku.radiotok.radioplayer.ext.from
import com.egoriku.radiotok.radioplayer.model.MediaPath.*
import kotlinx.coroutines.runBlocking

internal class MediaItemRepository(
    private val bitmapProvider: IBitmapProvider,
    private val stringResource: IStringResourceProvider,
    private val radioTokDb: RadioTokDb,
    private val tagsDataSource: ITagsDataSource,
    private val languagesDataSource: ILanguagesDataSource,
    private val countriesDataSource: ICountriesDataSource,
    private val radioMetadataDataSource: IRadioMetadataDataSource,
    private val topClicksDataSource: ITopClicksDataSource,
    private val topVoteDataSource: ITopVoteDataSource,
    private val localStationsDataSource: ILocalStationsDataSource,
    private val changedLatelyDataSource: IChangedLatelyDataSource,
    private val playingStationsDataSource: IPlayingStationsDataSource
) : IMediaItemRepository {

    private val dbMapper = DbStationToModelMapper()
    private val entityMapper = RadioEntityToModelMapper()

    override fun getRootItems() = listOf(
        browsableMediaItem {
            id = ShuffleAndPlayRoot.path
            title = stringResource.shuffleAndPlay
            iconBitmap = bitmapProvider.icRadioWaves
        },
        browsableMediaItem {
            id = PersonalPlaylistsRoot.path
            title = stringResource.personalPlaylists
            iconBitmap = bitmapProvider.icPersonal
        },
        browsableMediaItem {
            id = SmartPlaylistsRoot.path
            title = stringResource.smartPlaylists
            iconBitmap = bitmapProvider.icSmartPlaylist
        },
        browsableMediaItem {
            id = CatalogRoot.path
            title = stringResource.catalog
            iconBitmap = bitmapProvider.icCollection
        }
    )

    @OptIn(ExperimentalStdlibApi::class)
    override fun getShuffleAndPlayItems() = buildList {
        add(
            playableMediaItem {
                id = ShuffleAndPlayRoot.ShuffleRandom.path
                title = stringResource.randomRadio
                iconBitmap = bitmapProvider.icShuffleRound
            }
        )

        runBlocking {
            val mediaItem = if (radioTokDb.stationDao().likedStationsCount() > 0) {
                playableMediaItem {
                    id = ShuffleAndPlayRoot.ShuffleLiked.path
                    title = stringResource.likedRadio
                    iconBitmap = bitmapProvider.icLikedRound
                }
            } else {
                browsableMediaItem {
                    id = ShuffleAndPlayRoot.ShuffleLiked.path
                    title = stringResource.likedRadio
                    iconBitmap = bitmapProvider.icLikedRound
                }
            }

            add(mediaItem)
        }
    }

    override fun getPersonalPlaylistsItems() = listOf(
        browsableMediaItem {
            id = PersonalPlaylistsRoot.Liked.path
            title = stringResource.liked
            iconBitmap = bitmapProvider.icLikedRound
        },
        browsableMediaItem {
            id = PersonalPlaylistsRoot.RecentlyPlayed.path
            title = stringResource.recentlyPlayed
            iconBitmap = bitmapProvider.icHistoryRound
        },
        browsableMediaItem {
            id = PersonalPlaylistsRoot.Disliked.path
            title = stringResource.disliked
            iconBitmap = bitmapProvider.icDislikedRound
        }
    )

    override fun getLikedItems() = runBlocking {
        radioMetadataDataSource.loadByIds(
            ids = radioTokDb.stationDao().getLikedStationsIds()
        ).map {
            playableMediaItem {
                id = it.stationUuid
                title = it.name
                subTitle = it.tags
                iconUri = it.icon.toUri()

                appearance {
                    showAsList = true
                }
            }
        }
    }

    override fun getRecentlyPlayedItems(): List<MediaBrowserCompat.MediaItem> {
        return emptyList()
    }

    override fun getDislikedItems() = runBlocking {
        radioTokDb.stationDao().getDislikedStations().map {
            playableMediaItem {
                id = it.stationUuid
                title = it.name
                iconUri = it.icon.toUri()
                subTitle = it.tags
            }
        }
    }

    override fun getSmartPlaylistsItems() = listOf(
        browsableMediaItem {
            id = SmartPlaylistsRoot.LocalStations.path
            title = stringResource.localStations
            iconBitmap = bitmapProvider.icLocalRound
        },
        browsableMediaItem {
            id = SmartPlaylistsRoot.TopClicks.path
            title = stringResource.topClicks
            iconBitmap = bitmapProvider.icTopClicksRound
        },
        browsableMediaItem {
            id = SmartPlaylistsRoot.TopVote.path
            title = stringResource.topVote
            iconBitmap = bitmapProvider.icTopVoteRound
        },
        browsableMediaItem {
            id = SmartPlaylistsRoot.ChangedLately.path
            title = stringResource.changedLately
            iconBitmap = bitmapProvider.icChangedLatelyRound
        },
        browsableMediaItem {
            id = SmartPlaylistsRoot.Playing.path
            title = stringResource.playing
            iconBitmap = bitmapProvider.icPlayingRound
        }
    )

    override fun getLocalItems() = runBlocking {
        localStationsDataSource.load().map { radioEntity ->
            playableMediaItem {
                id = radioEntity.stationUuid
                title = radioEntity.name
                iconUri = radioEntity.icon.toUri()
            }
        }
    }

    override fun getCatalogItems() = listOf(
        browsableMediaItem {
            id = CatalogRoot.ByTags.path
            title = stringResource.byTags
            iconBitmap = bitmapProvider.icTagsRound
        },
        browsableMediaItem {
            id = CatalogRoot.ByCountries.path
            title = stringResource.byCountry
            iconBitmap = bitmapProvider.icCountryRounded
        },
        browsableMediaItem {
            id = CatalogRoot.ByLanguages.path
            title = stringResource.byLanguage
            iconBitmap = bitmapProvider.icLanguageRound
        }
    )

    override fun getTopClicksItems() = runBlocking {
        topClicksDataSource.load().map { radioEntity ->
            playableMediaItem {
                id = radioEntity.stationUuid
                title = radioEntity.name
                iconUri = radioEntity.icon.toUri()
            }
        }
    }

    override fun getTopVoteItems() = runBlocking {
        topVoteDataSource.load().map { radioEntity ->
            playableMediaItem {
                id = radioEntity.stationUuid
                title = radioEntity.name
                iconUri = radioEntity.icon.toUri()
            }
        }
    }

    override fun getChangedLatelyItems() = runBlocking {
        changedLatelyDataSource.load().map { radioEntity ->
            playableMediaItem {
                id = radioEntity.stationUuid
                title = radioEntity.name
                iconUri = radioEntity.icon.toUri()
            }
        }
    }

    override fun getPlayingItems() = runBlocking {
        playingStationsDataSource.load().map { radioEntity ->
            playableMediaItem {
                id = radioEntity.stationUuid
                title = radioEntity.name
                iconUri = radioEntity.icon.toUri()
            }
        }
    }

    override fun getCatalogTags() = runBlocking {
        tagsDataSource.getGroupedTags().map {
            browsableMediaItem {
                id = it.key
                title = it.key
                subTitle = stringResource.getStationsCount(it.value.size)

                appearance {
                    showAsList = true
                }
            }
        }
    }

    override fun getCatalogCountries() = runBlocking {
        countriesDataSource.load().map {
            browsableMediaItem {
                id = it.name
                title = it.name
                subTitle = stringResource.getStationsCount(it.count)

                appearance {
                    showAsList = true
                }
            }
        }
    }

    override fun getCatalogLanguages() = runBlocking {
        languagesDataSource.load().map {
            browsableMediaItem {
                id = it.name
                title = it.name
                subTitle = stringResource.getStationsCount(it.count)
                iconUri = bitmapProvider.bgRadioGradient

                appearance {
                    showAsList = true
                }
            }
        }
    }

    override suspend fun getRandomItem(): MediaMetadataCompat {
        val randomStation = radioTokDb.stationDao().getRandomStation()

        return MediaMetadataCompat.Builder().from(
            itemModel = dbMapper.invoke(randomStation)
        ).build()
    }

    override suspend fun getLikedItem(): MediaMetadataCompat {
        val randomStation = radioTokDb.stationDao().getRandomLikedStation()

        return MediaMetadataCompat.Builder().from(
            itemModel = dbMapper.invoke(randomStation)
        ).build()
    }

    override suspend fun loadByStationId(id: String): MediaMetadataCompat =
        runBlocking {
            val stationById = radioMetadataDataSource.loadByIds(listOf(id)).first()

            MediaMetadataCompat.Builder().from(
                itemModel = entityMapper.invoke(stationById)
            ).build()
        }
}