package com.khoi.swipebeats.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.khoi.swipebeats.explore.Track
import com.khoi.swipebeats.explore.TrackListItem

@Composable
fun FavoritesScreen(
    onTrackClick: (Track) -> Unit,
    modifier: Modifier = Modifier
) {
    val favoriteTracks = FavoriteTracksStore.getFavorites()

    if (favoriteTracks.isEmpty()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "No favorites yet",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Add tracks to your favorites to see them here",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(favoriteTracks) { track ->
                TrackListItem(
                    track = track,
                    isFavorite = true,
                    onClick = {
                        onTrackClick(track)
                    },
                    onFavoriteClick = {
                        FavoriteTracksStore.toggleFavorite(track)
                    }
                )
            }
        }
    }
}
