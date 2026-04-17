package com.khoi.swipebeats.favorites.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.khoi.swipebeats.explore.Track

@Entity(tableName = "favorite_tracks")
data class FavoriteTrackEntity(
    @PrimaryKey
    val id: Long,
    val title: String,
    val artistName: String,
    val previewUrl: String?,
    val artworkUrl: String?
)

fun FavoriteTrackEntity.toTrack(): Track {
    return Track(
        id = id,
        title = title,
        artistName = artistName,
        previewUrl = previewUrl,
        artworkUrl = artworkUrl
    )
}

fun Track.toFavoriteTrackEntity(): FavoriteTrackEntity {
    return FavoriteTrackEntity(
        id = id,
        title = title,
        artistName = artistName,
        previewUrl = previewUrl,
        artworkUrl = artworkUrl
    )
}