package com.khoi.swipebeats.explore

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ExploreViewModel : ViewModel() {

    private val sampleTracks = listOf(
        Track(
            id = 1,
            title = "Blinding Lights",
            artistName = "The Weekend",
            previewUrl = null,
            artworkUrl = null
        ),
        Track(
            id = 2,
            title = "Levitating",
            artistName = "Dua Lipa",
            previewUrl = null,
            artworkUrl = null
        ),
        Track(
            id = 3,
            title = "As It Was",
            artistName = "Harry Styles",
            previewUrl = null,
            artworkUrl = null
        ),
        Track(
            id = 4,
            title = "Starboy",
            artistName = "The Weekend",
            previewUrl = null,
            artworkUrl = null
        ),
        Track(
            id = 5,
            title = "Watermelon Sugar",
            artistName = "Harry Styles",
            previewUrl = null,
            artworkUrl = null
        )
    )

    var query by mutableStateOf("")
        private set

    var uiState by mutableStateOf<ExploreUiState>(ExploreUiState.Empty)
        private set

    fun onQueryChange(newQuery: String) {
        query = newQuery

        val trimmedQuery = newQuery.trim()

        if (trimmedQuery.length < 2) {
            uiState = ExploreUiState.Empty
            return
        }

        val filteredTracks = sampleTracks.filter { track ->
            track.title.contains(trimmedQuery, ignoreCase = true) ||
                track.artistName.contains(trimmedQuery, ignoreCase = true)
        }

        uiState = if (filteredTracks.isEmpty()) {
            ExploreUiState.Error("No tracks found for \"$trimmedQuery\"")
        } else {
            ExploreUiState.Content(filteredTracks)
        }
    }
}