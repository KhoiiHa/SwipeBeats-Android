package com.khoi.swipebeats.itunes

data class ITunesSearchResponseDto(
    val resultCount: Int,
    val results: List<ITunesTrackDto>
)
