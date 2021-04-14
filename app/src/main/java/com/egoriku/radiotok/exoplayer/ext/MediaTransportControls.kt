@file:Suppress("NOTHING_TO_INLINE")

package com.egoriku.radiotok.exoplayer.ext

import android.support.v4.media.session.MediaControllerCompat.TransportControls
import androidx.core.os.bundleOf
import com.egoriku.radiotok.exoplayer.notification.CustomAction.ACTION_DISLIKE
import com.egoriku.radiotok.exoplayer.notification.CustomAction.ACTION_SKIP_TO_NEXT
import com.egoriku.radiotok.exoplayer.notification.CustomAction.ACTION_TOGGLE_FAVORITE

inline fun TransportControls.sendDislikeAction() {
    sendCustomAction(ACTION_DISLIKE, bundleOf())
}

inline fun TransportControls.sendSkipToNextAction() {
    sendCustomAction(ACTION_SKIP_TO_NEXT, bundleOf())
}

inline fun TransportControls.sendLikeAction() {
    sendCustomAction(ACTION_TOGGLE_FAVORITE, bundleOf())
}