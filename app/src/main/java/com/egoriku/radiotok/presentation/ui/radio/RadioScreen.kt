package com.egoriku.radiotok.presentation.ui.radio

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.egoriku.radiotok.domain.model.RadioItemModel
import com.egoriku.radiotok.presentation.ControlsActions
import com.egoriku.radiotok.presentation.state.RadioPlaybackState
import com.egoriku.radiotok.presentation.ui.radio.about.TopSection
import com.egoriku.radiotok.presentation.ui.radio.controls.PlayerControls

@Composable
fun RadioScreen(
    radioItemModel: RadioItemModel,
    playbackState: RadioPlaybackState,
    controlsActions: ControlsActions
) {
    Surface {
        Player(
            topSection = {
                TopSection(logoUrl = radioItemModel.icon, title = radioItemModel.name)
            },
            playerControls = {
                PlayerControls(
                    isPlaying = playbackState.isPlaying,
                    controlsActions = controlsActions
                )
            }
        )
    }
}