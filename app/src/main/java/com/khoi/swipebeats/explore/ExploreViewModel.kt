package com.khoi.swipebeats.explore

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ExploreViewModel : ViewModel() {

    var query by mutableStateOf("")
        private set

    var uiState by mutableStateOf<ExploreUiState>(ExploreUiState.Empty)
        private set

    fun onQueryChange(newQuery: String) {
        query = newQuery

        val trimmedQuery = newQuery.trim()

        uiState = when {
            trimmedQuery.isEmpty() -> ExploreUiState.Empty
            trimmedQuery.length < 2 -> ExploreUiState.Empty
            else -> ExploreUiState.Content
        }
    }

    fun showLoading() {
        uiState = ExploreUiState.Loading
    }

    fun showError(message: String) {
        uiState = ExploreUiState.Error(message)
    }

    fun showContent() {
        uiState = ExploreUiState.Content
    }

    fun showEmpty() {
        uiState = ExploreUiState.Empty
    }
}