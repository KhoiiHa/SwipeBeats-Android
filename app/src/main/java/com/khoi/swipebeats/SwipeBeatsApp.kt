package com.khoi.swipebeats

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.khoi.swipebeats.explore.ExploreScreen
import com.khoi.swipebeats.explore.Track
import com.khoi.swipebeats.explore.TrackDetailScreen
import com.khoi.swipebeats.favorites.FavoriteTracksStore
import com.khoi.swipebeats.favorites.FavoritesScreen
import com.khoi.swipebeats.player.PreviewPlayerManager
import com.khoi.swipebeats.swipe.SwipeScreen

// Simple screen enum for MVP (no Navigation Compose yet)
enum class BottomTab {
    SWIPE, EXPLORE, FAVORITES, PLAYLISTS
}

@Composable
fun SwipeBeatsApp() {

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        FavoriteTracksStore.initialize(context)
    }

    val previewPlayerManager = remember {
        PreviewPlayerManager(context)
    }

    var selectedTab by remember { mutableStateOf(BottomTab.EXPLORE) }
    var selectedTrack by remember { mutableStateOf<Track?>(null) }
    var currentPreviewTrack by remember { mutableStateOf<Track?>(null) }
    var isMiniPlayerPlaying by remember { mutableStateOf(false) }

    DisposableEffect(previewPlayerManager) {
        previewPlayerManager.setOnIsPlayingChangedListener { isPlaying ->
            isMiniPlayerPlaying = isPlaying
        }

        previewPlayerManager.setOnCurrentPreviewChangedListener { previewUrl ->
            if (previewUrl == null) {
                currentPreviewTrack = null
                isMiniPlayerPlaying = false
            }
        }

        onDispose {
            previewPlayerManager.release()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Column {
                currentPreviewTrack?.let { previewTrack ->
                    MiniPlayerBar(
                        track = previewTrack,
                        isPlaying = isMiniPlayerPlaying,
                        onTogglePlayback = {
                            previewPlayerManager.playOrToggle(previewTrack.previewUrl)
                        },
                        onDismiss = {
                            previewPlayerManager.stop()
                        },
                        onOpenDetails = {
                            selectedTrack = previewTrack
                        }
                    )
                }

                if (selectedTrack == null) {
                    NavigationBar {
                        NavigationBarItem(
                            selected = selectedTab == BottomTab.SWIPE,
                            onClick = { selectedTab = BottomTab.SWIPE },
                            icon = { Icon(Icons.Outlined.PlayArrow, contentDescription = "Swipe") },
                            label = { Text("Swipe") }
                        )
                        NavigationBarItem(
                            selected = selectedTab == BottomTab.EXPLORE,
                            onClick = { selectedTab = BottomTab.EXPLORE },
                            icon = { Icon(Icons.Outlined.Explore, contentDescription = "Explore") },
                            label = { Text("Explore") }
                        )
                        NavigationBarItem(
                            selected = selectedTab == BottomTab.FAVORITES,
                            onClick = { selectedTab = BottomTab.FAVORITES },
                            icon = { Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Favorites") },
                            label = { Text("Favorites") }
                        )
                        NavigationBarItem(
                            selected = selectedTab == BottomTab.PLAYLISTS,
                            onClick = { selectedTab = BottomTab.PLAYLISTS },
                            icon = { Icon(Icons.Outlined.LibraryMusic, contentDescription = "Playlists") },
                            label = { Text("Playlists") }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            if (selectedTrack != null) {
                TrackDetailScreen(
                    track = selectedTrack!!,
                    previewPlayerManager = previewPlayerManager,
                    isPreviewPlaying = currentPreviewTrack?.previewUrl == selectedTrack!!.previewUrl &&
                        isMiniPlayerPlaying,
                    onPreviewTrackChanged = { track ->
                        currentPreviewTrack = track
                    },
                    onBack = {
                        selectedTrack = null
                    }
                )
            } else {
                when (selectedTab) {
                    BottomTab.SWIPE -> SwipeScreen(
                        previewPlayerManager = previewPlayerManager,
                        onPreviewTrackChanged = { track ->
                            currentPreviewTrack = track
                        }
                    )
                    BottomTab.EXPLORE -> ExploreScreen(
                        onTrackClick = { track ->
                            selectedTrack = track
                        }
                    )
                    BottomTab.FAVORITES -> FavoritesScreen(
                        onTrackClick = { track ->
                            selectedTrack = track
                        }
                    )
                    BottomTab.PLAYLISTS -> Text("Playlists Screen")
                }
            }
        }
    }
}

@Composable
private fun MiniPlayerBar(
    track: Track,
    isPlaying: Boolean,
    onTogglePlayback: () -> Unit,
    onDismiss: () -> Unit,
    onOpenDetails: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onOpenDetails)
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = track.artworkUrl,
                contentDescription = track.title,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(14.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.size(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = track.title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = track.artistName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onTogglePlayback) {
                    Icon(
                        imageVector = if (isPlaying) {
                            Icons.Outlined.Pause
                        } else {
                            Icons.Outlined.PlayArrow
                        },
                        contentDescription = if (isPlaying) {
                            "Pause preview"
                        } else {
                            "Play preview"
                        }
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Close mini player"
                    )
                }
            }
        }
    }
}
