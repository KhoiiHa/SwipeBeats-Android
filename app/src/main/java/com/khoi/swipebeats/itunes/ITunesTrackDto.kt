package com.khoi.swipebeats.itunes

data class ITunesTrackDto(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val artworkUrl100: String?,
    val previewUrl: String?
)
