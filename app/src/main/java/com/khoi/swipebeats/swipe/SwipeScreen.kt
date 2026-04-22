package com.khoi.swipebeats.swipe

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun SwipeScreen() {

    val mockTracks = listOf(
        Track(
            id = 1L,
            title = "Levitating",
            artistName = "Dua Lipa",
            artworkUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music126/v4/6f/74/5d/6f745d19-35f3-1f53-15de-6ec8f7a4d3a0/190295132531.jpg/600x600bb.jpg",
            previewUrl = null
        ),
        Track(
            id = 2L,
            title = "Blinding Lights",
            artistName = "The Weeknd",
            artworkUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music124/v4/56/af/a6/56afa653-2dbf-28e3-2320-55cf0fc30d8e/20UMGIM08985.rgb.jpg/600x600bb.jpg",
            previewUrl = null
        ),
        Track(
            id = 3L,
            title = "Watermelon Sugar",
            artistName = "Harry Styles",
            artworkUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music124/v4/a3/1f/ef/a31fefb4-a27d-21ea-235e-0d9fcb13b70c/886448276787.jpg/600x600bb.jpg",
            previewUrl = null
        ),
        Track(
            id = 4L,
            title = "Peaches & Cream",
            artistName = "Lofi Beats",
            artworkUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/ff/44/91/ff4491e1-6c6d-8c2a-9b91-9c8a1e5c1b4e/cover.jpg/600x600bb.jpg",
            previewUrl = null
        ),
        Track(
            id = 5L,
            title = "Sunflower",
            artistName = "Post Malone",
            artworkUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music118/v4/2c/8a/4d/2c8a4d9f-b4a5-57d8-94d1-3f7d0f6c4e91/18UMGIM45753.rgb.jpg/600x600bb.jpg",
            previewUrl = null
        ),
        Track(
            id = 6L,
            title = "Stay",
            artistName = "The Kid LAROI",
            artworkUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/5b/3d/9e/5b3d9e3f-7b6a-1e34-3a5b-1d1c9b6f8a3d/21UMGIM75467.rgb.jpg/600x600bb.jpg",
            previewUrl = null
        ),
        Track(
            id = 7L,
            title = "Golden Hour",
            artistName = "JVKE",
            artworkUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music112/v4/0c/21/7a/0c217a4d-9b3a-5c2e-8b2a-3c1d9a8b7c4e/22UMGIM12345.rgb.jpg/600x600bb.jpg",
            previewUrl = null
        ),
        Track(
            id = 8L,
            title = "Circles",
            artistName = "Post Malone",
            artworkUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music124/v4/aa/bb/cc/aabbccdd-eeff-0011-2233-445566778899/19UMGIM76543.rgb.jpg/600x600bb.jpg",
            previewUrl = null
        )
    )

    var currentIndex by remember { mutableStateOf(0) }
    var likedTrackIds by remember { mutableStateOf(setOf<Long>()) }
    var rejectedTrackIds by remember { mutableStateOf(setOf<Long>()) }
    var offsetX by remember { mutableStateOf(0f) }

    val currentTrack = mockTracks.getOrNull(currentIndex)

    fun showNextTrack() {
        if (currentIndex < mockTracks.lastIndex) {
            currentIndex += 1
        }
        offsetX = 0f
    }

    fun likeCurrentTrack() {
        currentTrack?.let { track ->
            likedTrackIds = likedTrackIds + track.id
            rejectedTrackIds = rejectedTrackIds - track.id
        }
        showNextTrack()
    }

    fun rejectCurrentTrack() {
        currentTrack?.let { track ->
            rejectedTrackIds = rejectedTrackIds + track.id
            likedTrackIds = likedTrackIds - track.id
        }
        showNextTrack()
    }

    val swipeThreshold = 160f

    if (currentTrack == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "No more tracks",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            Text(
                text = "You reached the end of the swipe stack",
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

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .graphicsLayer {
                    translationX = offsetX
                    rotationZ = offsetX / 40f
                }
                .pointerInput(currentTrack.id) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount.x
                        },
                        onDragEnd = {
                            when {
                                offsetX > swipeThreshold -> likeCurrentTrack()
                                offsetX < -swipeThreshold -> rejectCurrentTrack()
                                else -> offsetX = 0f
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
                if (offsetX > 40f) {
                    Text(
                        text = "LIKE",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                } else if (offsetX < -40f) {
                    Text(
                        text = "NOPE",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 12.dp)
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
            Button(onClick = { rejectCurrentTrack() }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Nope"
                    )
                    Text(text = "Nope", modifier = Modifier.padding(start = 6.dp))
                }
            }

            Button(onClick = { likeCurrentTrack() }) {
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