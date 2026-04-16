package com.khoi.swipebeats.explore

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.khoi.swipebeats.player.PreviewPlayerManager
import com.khoi.swipebeats.favorites.FavoriteTracksStore

@Composable
fun TrackDetailScreen(
    track: Track,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val previewPlayerManager = remember {
        PreviewPlayerManager(context)
    }
    val isPlayingState = remember { mutableStateOf(false) }

    val isFavorite = FavoriteTracksStore.isFavorite(track.id)

    DisposableEffect(previewPlayerManager, track.previewUrl) {
        previewPlayerManager.setOnIsPlayingChangedListener { isPlaying ->
            isPlayingState.value = previewPlayerManager.isCurrentPreview(track.previewUrl) && isPlaying
        }

        onDispose {
            previewPlayerManager.release()
            isPlayingState.value = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "← Back",
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onBack() }
                .padding(bottom = 16.dp),
            style = MaterialTheme.typography.bodyMedium
        )

        AsyncImage(
            model = track.artworkUrl,
            contentDescription = track.title,
            modifier = Modifier
                .size(220.dp)
                .clip(RoundedCornerShape(20.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = track.title,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = track.artistName,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                FavoriteTracksStore.toggleFavorite(track)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (isFavorite) {
                    "Remove from Favorites"
                } else {
                    "Add to Favorites"
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Preview",
                style = MaterialTheme.typography.titleMedium
            )

            Button(
                onClick = {
                    previewPlayerManager.playOrToggle(track.previewUrl)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (isPlayingState.value) {
                        "Pause Preview"
                    } else {
                        "Play Preview"
                    }
                )
            }

            Text(
                text = track.previewUrl ?: "No preview available",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
