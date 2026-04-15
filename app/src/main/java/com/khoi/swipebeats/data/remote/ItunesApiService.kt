package com.khoi.swipebeats.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApiService {

    @GET("search")
    suspend fun searchTracks(
        @Query("term") query: String,
        @Query("entity") entity: String = "song",
        @Query("limit") limit: Int = 25
    ): ItunesResponse
}