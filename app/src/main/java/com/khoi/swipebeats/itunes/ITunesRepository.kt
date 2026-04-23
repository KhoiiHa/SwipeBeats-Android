package com.khoi.swipebeats.itunes

import com.khoi.swipebeats.explore.Track

class ITunesRepository(
    private val apiService: ITunesApiService
) {

    suspend fun searchSongs(term: String, limit: Int = 20): List<Track> {
        val response = apiService.searchSongs(term = term, limit = limit)
        return response.results.map { it.toTrack() }
    }
}
