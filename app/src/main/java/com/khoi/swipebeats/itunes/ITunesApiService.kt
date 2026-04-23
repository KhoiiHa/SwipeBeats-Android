package com.khoi.swipebeats.itunes

import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApiService {

    @GET("search")
    suspend fun searchSongs(
        @Query("term") term: String,
        @Query("limit") limit: Int = 20,
        @Query("media") media: String = "music",
        @Query("entity") entity: String = "song"
    ): ITunesSearchResponseDto
}
