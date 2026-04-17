package com.khoi.swipebeats.explore

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.khoi.swipebeats.favorites.FavoriteTracksStore

@Composable
fun ExploreScreen(
    onTrackClick: (Track) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExploreViewModel = viewModel()
) {
    val query = viewModel.query
    val uiState = viewModel.uiState

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Explore")

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = query,
            onValueChange = viewModel::onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search for songs or artists") }
        )

        Spacer(modifier = Modifier.height(24.dp))

        when (val state = uiState) {
            ExploreUiState.Empty -> {
                Text(text = "No results yet")
            }

            ExploreUiState.Loading -> {
                Text(text = "Loading...")
            }

            is ExploreUiState.Error -> {
                Text(text = state.message)
            }

            is ExploreUiState.Content -> {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = state.tracks,
                        key = { track -> track.id }
                    ) { track ->
                        TrackListItem(
                            track = track,
                            isFavorite = FavoriteTracksStore.isFavorite(track.id),
                            onClick = {
                                onTrackClick(track)
                            }
                        )
                    }
                }
            }
        }
    }
}