package com.khoi.swipebeats.favorites

import androidx.compose.runtime.mutableStateOf
import com.khoi.swipebeats.explore.Track

object FavoriteTracksStore {

    private val favoriteTracksState = mutableStateOf<List<Track>>(emptyList())

    fun isFavorite(trackId: Long): Boolean {
        return favoriteTracksState.value.any { track ->
            track.id == trackId
        }
    }

    fun toggleFavorite(track: Track) {
        val current = favoriteTracksState.value

        favoriteTracksState.value = if (isFavorite(track.id)) {
            current.filter { favoriteTrack ->
                favoriteTrack.id != track.id
            }
        } else {
            current + track
        }
    }

    fun getFavorites(): List<Track> {
        return favoriteTracksState.value
    }
}
