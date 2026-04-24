package com.khoi.swipebeats.swipe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khoi.swipebeats.explore.Track
import com.khoi.swipebeats.itunes.ITunesRepository
import kotlinx.coroutines.launch

class SwipeViewModel(
    private val repository: ITunesRepository
) : ViewModel() {

    var tracks by mutableStateOf<List<Track>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var currentIndex by mutableStateOf(0)
        private set

    var likedTrackIds by mutableStateOf(setOf<Long>())
        private set

    var rejectedTrackIds by mutableStateOf(setOf<Long>())
        private set

    val currentTrack: Track?
        get() = tracks.getOrNull(currentIndex)

    val nextTrack: Track?
        get() = tracks.getOrNull(currentIndex + 1)

    fun loadTracks(term: String, limit: Int = 20) {
        val trimmedTerm = term.trim()

        if (trimmedTerm.isEmpty()) return

        viewModelScope.launch {
            isLoading = true

            try {
                val loadedTracks = repository.searchSongs(
                    term = trimmedTerm,
                    limit = limit
                )

                tracks = loadedTracks
                currentIndex = 0
                likedTrackIds = emptySet()
                rejectedTrackIds = emptySet()
            } catch (exception: Exception) {
                tracks = emptyList()
                currentIndex = 0
                likedTrackIds = emptySet()
                rejectedTrackIds = emptySet()
            } finally {
                isLoading = false
            }
        }
    }

    fun likeCurrentTrack() {
        val track = currentTrack ?: return

        likedTrackIds = likedTrackIds + track.id
        rejectedTrackIds = rejectedTrackIds - track.id
        moveToNextTrack()
    }

    fun rejectCurrentTrack() {
        val track = currentTrack ?: return

        rejectedTrackIds = rejectedTrackIds + track.id
        likedTrackIds = likedTrackIds - track.id
        moveToNextTrack()
    }

    private fun moveToNextTrack() {
        if (currentIndex < tracks.lastIndex) {
            currentIndex += 1
        } else {
            currentIndex = tracks.size
        }
    }
}
