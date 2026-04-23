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

    fun loadTracks(term: String, limit: Int = 20) {
        val trimmedTerm = term.trim()

        if (trimmedTerm.isEmpty()) return

        viewModelScope.launch {
            isLoading = true

            tracks = repository.searchSongs(
                term = trimmedTerm,
                limit = limit
            )

            isLoading = false
        }
    }
}
