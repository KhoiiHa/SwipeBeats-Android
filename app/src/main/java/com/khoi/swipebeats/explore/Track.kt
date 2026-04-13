package com.khoi.swipebeats.explore

data class Track(
    val id: Long,
    val title: String,
    val artistName: String,
    val previewUrl: String? = null,
    val artworkUrl: String? = null
)
