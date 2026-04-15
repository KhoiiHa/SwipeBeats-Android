package com.khoi.swipebeats.explore

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khoi.swipebeats.data.ItunesRepository
import com.khoi.swipebeats.data.remote.RetrofitInstance
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ExploreViewModel : ViewModel() {

    private val repository = ItunesRepository(api = RetrofitInstance.api)

    var query by mutableStateOf("")
        private set

    var uiState by mutableStateOf<ExploreUiState>(ExploreUiState.Empty)
        private set

    private var searchJob: Job? = null

    fun onQueryChange(newQuery: String) {
        query = newQuery

        val trimmedQuery = newQuery.trim()

        searchJob?.cancel()

        if (trimmedQuery.length < 2) {
            uiState = ExploreUiState.Empty
            return
        }

        uiState = ExploreUiState.Loading

        searchJob = viewModelScope.launch {
            delay(300)

            try {
                val tracks = repository.searchTracks(query = trimmedQuery)

                uiState = if (tracks.isEmpty()) {
                    ExploreUiState.Error("No tracks found for \"$trimmedQuery\"")
                } else {
                    ExploreUiState.Content(tracks)
                }
            } catch (exception: Exception) {
                uiState = ExploreUiState.Error(
                    message = exception.message ?: "Something went wrong while searching tracks"
                )
            }
        }
    }
}