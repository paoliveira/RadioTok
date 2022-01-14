package com.egoriku.radiotok.presentation.screen.feed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.egoriku.radiotok.R
import com.egoriku.radiotok.foundation.button.IconButton
import com.egoriku.radiotok.foundation.header.ScreenHeader
import com.egoriku.radiotok.presentation.screen.feed.ui.FeedRow
import com.egoriku.radiotok.presentation.screen.feed.ui.InstantRadio
import com.egoriku.radiotok.presentation.screen.feed.ui.PlaylistWithIcon
import com.egoriku.radiotok.presentation.screen.feed.ui.SimplePlaylist
import com.egoriku.radiotok.presentation.screen.playlist.PlaylistScreen
import com.egoriku.radiotok.presentation.screen.settings.SettingScreen
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import org.koin.androidx.compose.getViewModel

object FeedScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val feedViewModel = getViewModel<FeedViewModel>()

        val feedState by feedViewModel.feedState.collectAsState()

        when (
            val state: FeedState = feedState) {
            is FeedState.Loading -> {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(modifier = Modifier)
                    }
                }
            }
            is FeedState.Error -> {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Error loading")
                    }
                }
            }
            is FeedState.Success -> {
                Surface(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        contentPadding = rememberInsetsPaddingValues(
                            insets = LocalWindowInsets.current.systemBars,
                            applyTop = true,
                            applyBottom = true
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
                                    navigator.push(SettingScreen)
                                }
                            }
                        }
                        item {
                            FeedRow(title = stringResource(id = R.string.media_item_path_shuffle_and_play)) {
                                items(state.feed.shuffleAndPlay) {
                                    InstantRadio(feed = it) {
                                        feedViewModel.playFromMediaId(it.mediaId)
                                    }
                                }
                            }
                        }
                        item {
                            FeedRow(title = stringResource(id = R.string.media_item_path_personal_playlists)) {
                                items(state.feed.forYou) {
                                    PlaylistWithIcon(collection = it) { id ->
                                        navigator.push(
                                            PlaylistScreen(id = id)
                                        )
                                    }
                                }
                            }
                        }
                        item {
                            FeedRow(title = stringResource(id = R.string.media_item_path_smart_playlists)) {
                                items(state.feed.smartPlaylists) {
                                    PlaylistWithIcon(collection = it) { id ->
                                        navigator.push(
                                            PlaylistScreen(id = id)
                                        )
                                    }
                                }
                            }
                        }
                        item {
                            FeedRow(title = stringResource(id = R.string.media_item_path_by_tags)) {
                                items(state.feed.byTags) {
                                    SimplePlaylist(collection = it)
                                }
                            }
                        }
                        item {
                            FeedRow(title = stringResource(id = R.string.media_item_path_by_country)) {
                                items(state.feed.byCountry) {
                                    SimplePlaylist(collection = it)
                                }
                            }
                        }
                        item {
                            FeedRow(title = stringResource(id = R.string.media_item_path_by_language)) {
                                items(state.feed.byLanguage) {
                                    SimplePlaylist(collection = it)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}