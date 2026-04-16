
package com.khoi.swipebeats.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

class PreviewPlayerManager(
    context: Context
) {

    private val player: ExoPlayer = ExoPlayer.Builder(context).build()

    private var currentPreviewUrl: String? = null

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
    }

    fun release() {
        player.release()
        currentPreviewUrl = null
    }
}

