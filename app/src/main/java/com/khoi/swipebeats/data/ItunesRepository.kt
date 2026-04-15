package com.khoi.swipebeats.data

import com.khoi.swipebeats.data.remote.ItunesApiService
import com.khoi.swipebeats.data.remote.toTrack
import com.khoi.swipebeats.explore.Track

class ItunesRepository(
    private val api: ItunesApiService
) {

    suspend fun searchTracks(query: String): List<Track> {
        val response = api.searchTracks(query = query)

        return response.results.mapNotNull { dto ->
            dto.toTrack()
        }
    }
}