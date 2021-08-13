package com.egoriku.radiotok.presentation.screen.feed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.egoriku.radiotok.R
import com.egoriku.radiotok.common.ext.toFlagEmoji
import com.egoriku.radiotok.foundation.HSpacer
import com.egoriku.radiotok.foundation.button.IconButton
import com.egoriku.radiotok.presentation.screen.Navigator
import com.egoriku.radiotok.presentation.screen.feed.model.FeedType.*
import com.egoriku.radiotok.presentation.screen.feed.ui.FeedRow
import com.egoriku.radiotok.presentation.screen.feed.ui.InstantRadio
import com.egoriku.radiotok.presentation.ui.header.ScreenHeader
import com.egoriku.radiotok.radioplayer.constant.MediaBrowserConstant.SUB_PATH_SHUFFLE_LIKED
import com.egoriku.radiotok.radioplayer.constant.MediaBrowserConstant.SUB_PATH_SHUFFLE_RANDOM
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import org.koin.androidx.compose.getViewModel

@Composable
fun FeedScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(),
    navigator: Navigator
) {
    val feedViewModel = getViewModel<FeedViewModel>()

    val shuffleAndPlay = listOf(
        InstantPlay(
            mediaId = SUB_PATH_SHUFFLE_RANDOM,
            name = "Random",
            icon = R.drawable.ic_random
        ),
        InstantPlay(
            mediaId = SUB_PATH_SHUFFLE_LIKED,
            name = "Liked",
            icon = R.drawable.ic_favorite
        )
    )

    val forYou = listOf(
        Playlist(name = "Liked", icon = R.drawable.ic_favorite),
        Playlist(name = "Disliked", icon = R.drawable.ic_not_interested)
    )

    val smartPlaylists = listOf(
        SimplePlaylist(name = "Local stations"),
        SimplePlaylist(name = "Top clicks"),
        SimplePlaylist(name = "Top Vote"),
        SimplePlaylist(name = "Changed lately"),
        SimplePlaylist(name = "Playing"),
    )

    val byTags = listOf(
        SimplePlaylist("Folk"),
        SimplePlaylist("Dance"),
        SimplePlaylist("News")
    )

    val byCountry = listOf(
        SimplePlaylist("Pl".toFlagEmoji),
        SimplePlaylist("US".toFlagEmoji),
        SimplePlaylist("NL".toFlagEmoji)
    )

    val byLanguage = listOf(
        SimplePlaylist("80er"),
        SimplePlaylist("akan"),
        SimplePlaylist("all")
    )

    Surface(modifier = modifier) {
        LazyColumn(
            contentPadding = rememberInsetsPaddingValues(
                insets = LocalWindowInsets.current.systemBars,
                applyTop = true,
                applyBottom = true,
                additionalBottom = paddingValues.calculateBottomPadding()
            )
        ) {
            item {
                ScreenHeader(
                    title = stringResource(id = R.string.app_name)
                ) {
                    IconButton(
                        modifier = Modifier.padding(end = 8.dp),
                        painter = painterResource(id = R.drawable.ic_settings),
                        contentDescription = stringResource(id = R.string.cc_open_settings)
                    ) {
                        navigator.openSettings()
                    }
                }
            }
            item {
                FeedRow(title = stringResource(id = R.string.media_item_path_shuffle_and_play)) {
                    items(shuffleAndPlay) {
                        InstantRadio(feed = it) {
                            feedViewModel.playFromMediaId(it.mediaId)
                        }
                    }
                }
            }
            item {
                FeedRow(title = stringResource(id = R.string.media_item_path_personal_playlists)) {
                    items(forYou) {
                        RadioItemPlaylist(collection = it)
                    }
                }
            }
            item {
                FeedRow(title = stringResource(id = R.string.media_item_path_smart_playlists)) {
                    items(smartPlaylists) {
                        RadioItemSimplePlaylist(collection = it)
                    }
                }
            }
            item {
                FeedRow(title = stringResource(id = R.string.media_item_path_by_genres)) {
                    items(byTags) {
                        RadioItemSimplePlaylist(collection = it)
                    }
                }
            }
            item {
                FeedRow(title = stringResource(id = R.string.media_item_path_by_country)) {
                    items(byCountry) {
                        RadioItemSimplePlaylist(collection = it)
                    }
                }
            }
            item {
                FeedRow(title = stringResource(id = R.string.media_item_path_by_language)) {
                    items(byLanguage) {
                        RadioItemSimplePlaylist(collection = it)
                    }
                }
            }
        }
    }
}

@Composable
fun RadioItemPlaylist(
    modifier: Modifier = Modifier,
    collection: Playlist
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        backgroundColor = MaterialTheme.colors.secondary,
        modifier = modifier.size(150.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable { },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (collection.icon != -1) {
                Icon(
                    painter = painterResource(id = collection.icon),
                    contentDescription = null
                )
                HSpacer(16.dp)
            }

            Text(text = collection.name, style = MaterialTheme.typography.body1)
        }
    }
}

@Composable
fun RadioItemSimplePlaylist(
    modifier: Modifier = Modifier,
    collection: SimplePlaylist
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        backgroundColor = MaterialTheme.colors.secondary,
        modifier = modifier.size(150.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable { },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = collection.name, style = MaterialTheme.typography.body1)
        }
    }
}