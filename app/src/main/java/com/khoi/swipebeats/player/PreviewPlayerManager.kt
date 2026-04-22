package com.khoi.swipebeats.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer

class PreviewPlayerManager(
    context: Context
) {

    private val player: ExoPlayer = ExoPlayer.Builder(context).build()

    private var currentPreviewUrl: String? = null

    private var onIsPlayingChanged: ((Boolean) -> Unit)? = null
    private var onCurrentPreviewChanged: ((String?) -> Unit)? = null

    private val playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            onIsPlayingChanged?.invoke(isPlaying)
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            if (playbackState == Player.STATE_ENDED) {
                currentPreviewUrl = null
                onIsPlayingChanged?.invoke(false)
                onCurrentPreviewChanged?.invoke(null)
            }
        }
    }

    init {
        player.addListener(playerListener)
    }

    fun setOnIsPlayingChangedListener(listener: (Boolean) -> Unit) {
        onIsPlayingChanged = listener
    }

    fun setOnCurrentPreviewChangedListener(listener: (String?) -> Unit) {
        onCurrentPreviewChanged = listener
    }

    fun isCurrentPreview(previewUrl: String?): Boolean {
        return currentPreviewUrl == previewUrl
    }

    fun isPlaying(): Boolean {
        return player.isPlaying
    }

    fun playOrToggle(previewUrl: String?) {
        if (previewUrl.isNullOrBlank()) return

        if (currentPreviewUrl == previewUrl) {
            if (player.isPlaying) {
                player.pause()
            } else {
                player.play()
            }
            return
        }

        currentPreviewUrl = previewUrl
        onCurrentPreviewChanged?.invoke(previewUrl)

        val mediaItem = MediaItem.fromUri(previewUrl)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    fun pause() {
        player.pause()
    }

    fun stop() {
        player.stop()
        currentPreviewUrl = null
        onIsPlayingChanged?.invoke(false)
        onCurrentPreviewChanged?.invoke(null)
    }

    fun release() {
        player.removeListener(playerListener)
        player.release()
        currentPreviewUrl = null
        onIsPlayingChanged?.invoke(false)
        onCurrentPreviewChanged?.invoke(null)
        onIsPlayingChanged = null
        onCurrentPreviewChanged = null
    }
}
