package com.khoi.swipebeats.explore

sealed interface ExploreUiState {
    data object Empty : ExploreUiState
    data object Loading : ExploreUiState
    data class Error(val message: String) : ExploreUiState
    data class Content(val tracks: List<Track>) : ExploreUiState
}