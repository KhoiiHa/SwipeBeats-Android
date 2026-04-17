package com.khoi.swipebeats.favorites

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import com.khoi.swipebeats.explore.Track
import com.khoi.swipebeats.favorites.local.FavoritesDatabase
import com.khoi.swipebeats.favorites.local.toFavoriteTrackEntity
import com.khoi.swipebeats.favorites.local.toTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

object FavoriteTracksStore {

    private val favoriteTracksState = mutableStateOf<List<Track>>(emptyList())
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var isInitialized = false

    private lateinit var applicationContext: Context

    fun initialize(context: Context) {
        if (isInitialized) return
        isInitialized = true

        applicationContext = context.applicationContext
        val dao = FavoritesDatabase.getInstance(context).favoriteTrackDao()

        scope.launch {
            dao.getFavoriteTracks().collectLatest { favoriteEntities ->
                favoriteTracksState.value = favoriteEntities.map { entity ->
                    entity.toTrack()
                }
            }
        }
    }

    fun isFavorite(trackId: Long): Boolean {
        return favoriteTracksState.value.any { track ->
            track.id == trackId
        }
    }

    fun toggleFavorite(track: Track) {
        if (!isInitialized) return

        val trackId = track.id
        val favoriteTrackDao = FavoritesDatabase
            .getInstance(applicationContext)
            .favoriteTrackDao()

        scope.launch {
            if (favoriteTrackDao.isFavorite(trackId)) {
                favoriteTrackDao.deleteFavoriteTrack(trackId)
            } else {
                favoriteTrackDao.insertFavoriteTrack(track.toFavoriteTrackEntity())
            }
        }
    }

    fun getFavorites(): List<Track> {
        return favoriteTracksState.value
    }
}
