package com.khoi.swipebeats.swipe

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.zIndex
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.khoi.swipebeats.explore.Track
import com.khoi.swipebeats.player.PreviewPlayerManager
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.khoi.swipebeats.itunes.ITunesRepository
import com.khoi.swipebeats.itunes.RetrofitInstance

@Composable
fun SwipeScreen(
    previewPlayerManager: PreviewPlayerManager,
    onPreviewTrackChanged: (Track?) -> Unit
) {

    val swipeViewModel: SwipeViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SwipeViewModel(
                    repository = ITunesRepository(apiService = RetrofitInstance.api)
                ) as T
            }
        }
    )

    var isAnimating by remember { mutableStateOf(false) }
    val offsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    val tracks = swipeViewModel.tracks
    val isLoading = swipeViewModel.isLoading
    val currentTrack = swipeViewModel.currentTrack
    val nextTrack = swipeViewModel.nextTrack
    val likedTrackIds = swipeViewModel.likedTrackIds
    val rejectedTrackIds = swipeViewModel.rejectedTrackIds

    LaunchedEffect(Unit) {
        if (tracks.isEmpty()) {
            swipeViewModel.loadTracks(term = "pop", limit = 20)
        }
    }

    fun resetCardOffset() {
        scope.launch {
            offsetX.snapTo(0f)
        }
    }

    fun likeCurrentTrack() {
        swipeViewModel.likeCurrentTrack()
        resetCardOffset()
    }

    fun rejectCurrentTrack() {
        swipeViewModel.rejectCurrentTrack()
        resetCardOffset()
    }

    val swipeThreshold = 160f

    if (isLoading && tracks.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    if (currentTrack == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (tracks.isEmpty()) "No tracks found" else "No more tracks",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            Text(
                text = if (tracks.isEmpty()) "Try another search term later" else "You reached the end of the swipe stack",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )

            Text(
                text = "Liked: ${likedTrackIds.size} • Nope: ${rejectedTrackIds.size}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            // Background Card (next track)
            if (nextTrack != null) {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            val progress = (abs(offsetX.value) / swipeThreshold).coerceIn(0f, 1f)
                            scaleX = 0.95f + (progress * 0.05f)
                            scaleY = 0.95f + (progress * 0.05f)
                            translationY = 20f * (1f - progress)
                        },
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        AsyncImage(
                            model = nextTrack.artworkUrl,
                            contentDescription = nextTrack.title,
                            modifier = Modifier.size(220.dp),
                            contentScale = ContentScale.Crop
                        )

                        Text(
                            text = nextTrack.title,
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 20.dp)
                        )

                        Text(
                            text = nextTrack.artistName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }
                }
            }

            // Top Card (current track)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(1f)
                    .graphicsLayer {
                        translationX = offsetX.value
                        rotationZ = offsetX.value / 40f
                    }
                    .pointerInput(currentTrack.id) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                if (!isAnimating) {
                                    change.consume()
                                    scope.launch {
                                        offsetX.snapTo(offsetX.value + dragAmount.x)
                                    }
                                }
                            },
                            onDragEnd = {
                                if (!isAnimating) {
                                    when {
                                        offsetX.value > swipeThreshold -> {
                                            isAnimating = true
                                            scope.launch {
                                                try {
                                                    offsetX.animateTo(
                                                        targetValue = 1000f,
                                                        animationSpec = spring(stiffness = Spring.StiffnessLow)
                                                    )
                                                    previewPlayerManager.stop()
                                                    likeCurrentTrack()
                                                } finally {
                                                    isAnimating = false
                                                }
                                            }
                                        }
                                        offsetX.value < -swipeThreshold -> {
                                            isAnimating = true
                                            scope.launch {
                                                try {
                                                    offsetX.animateTo(
                                                        targetValue = -1000f,
                                                        animationSpec = spring(stiffness = Spring.StiffnessLow)
                                                    )
                                                    previewPlayerManager.stop()
                                                    rejectCurrentTrack()
                                                } finally {
                                                    isAnimating = false
                                                }
                                            }
                                        }
                                        else -> {
                                            isAnimating = true
                                            scope.launch {
                                                try {
                                                    offsetX.animateTo(
                                                        targetValue = 0f,
                                                        animationSpec = spring(
                                                            stiffness = Spring.StiffnessMedium,
                                                            dampingRatio = Spring.DampingRatioMediumBouncy
                                                        )
                                                    )
                                                } finally {
                                                    isAnimating = false
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        )
                    },
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    val swipeProgress = (abs(offsetX.value) / swipeThreshold).coerceIn(0f, 1f)
                    if (offsetX.value > 40f) {
                        Text(
                            text = "LIKE",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(bottom = 12.dp)
                                .graphicsLayer {
                                    alpha = swipeProgress
                                    scaleX = 1f + (swipeProgress * 0.3f)
                                    scaleY = 1f + (swipeProgress * 0.3f)
                                    rotationZ = offsetX.value / 20f
                                }
                        )
                    } else if (offsetX.value < -40f) {
                        Text(
                            text = "NOPE",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                                .padding(bottom = 12.dp)
                                .graphicsLayer {
                                    alpha = swipeProgress
                                    scaleX = 1f + (swipeProgress * 0.3f)
                                    scaleY = 1f + (swipeProgress * 0.3f)
                                    rotationZ = offsetX.value / 20f
                                }
                        )
                    }

                    AsyncImage(
                        model = currentTrack.artworkUrl,
                        contentDescription = currentTrack.title,
                        modifier = Modifier.size(220.dp),
                        contentScale = ContentScale.Crop
                    )

                    Text(
                        text = currentTrack.title,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 20.dp)
                    )

                    Text(
                        text = currentTrack.artistName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 6.dp)
                    )

                    val isCurrentPreviewPlaying =
                        previewPlayerManager.isCurrentPreview(currentTrack.previewUrl) && previewPlayerManager.isPlaying()

                    Button(
                        enabled = !isAnimating && currentTrack.previewUrl != null,
                        onClick = {
                            currentTrack.previewUrl?.let { previewUrl ->
                                onPreviewTrackChanged(currentTrack)
                                previewPlayerManager.playOrToggle(previewUrl)
                            }
                        },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            androidx.compose.material3.Icon(
                                imageVector = if (isCurrentPreviewPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                                contentDescription = if (isCurrentPreviewPlaying) "Pause preview" else "Play preview"
                            )
                            Text(
                                text = if (isCurrentPreviewPlaying) "Pause" else "Preview",
                                modifier = Modifier.padding(start = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        Text(
            text = "Liked: ${likedTrackIds.size} • Nope: ${rejectedTrackIds.size}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                enabled = !isAnimating,
                onClick = {
                    isAnimating = true
                    scope.launch {
                        try {
                            offsetX.animateTo(
                                targetValue = -1000f,
                                animationSpec = spring(stiffness = Spring.StiffnessLow)
                            )
                            previewPlayerManager.stop()
                            rejectCurrentTrack()
                        } finally {
                            isAnimating = false
                        }
                    }
                }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Nope"
                    )
                    Text(text = "Nope", modifier = Modifier.padding(start = 6.dp))
                }
            }

            Button(
                enabled = !isAnimating,
                onClick = {
                    isAnimating = true
                    scope.launch {
                        try {
                            offsetX.animateTo(
                                targetValue = 1000f,
                                animationSpec = spring(stiffness = Spring.StiffnessLow)
                            )
                            previewPlayerManager.stop()
                            likeCurrentTrack()
                        } finally {
                            isAnimating = false
                        }
                    }
                }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Outlined.Favorite,
                        contentDescription = "Like"
                    )
                    Text(text = "Like", modifier = Modifier.padding(start = 6.dp))
                }
            }
        }
    }
}
