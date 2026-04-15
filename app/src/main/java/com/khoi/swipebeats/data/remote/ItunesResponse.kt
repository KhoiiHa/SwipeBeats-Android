package com.khoi.swipebeats.data.remote

import com.google.gson.annotations.SerializedName
import com.khoi.swipebeats.explore.Track

data class ItunesResponse(
    @SerializedName("results")
    val results: List<ItunesTrackDto>
)

data class ItunesTrackDto(
    @SerializedName("trackId")
    val trackId: Long?,

    @SerializedName("trackName")
    val trackName: String?,

    @SerializedName("artistName")
    val artistName: String?,

    @SerializedName("previewUrl")
    val previewUrl: String?,

    @SerializedName("artworkUrl100")
    val artworkUrl100: String?
)

fun ItunesTrackDto.toTrack(): Track? {
    val id = trackId ?: return null
    val title = trackName ?: return null
    val artist = artistName ?: return null

    return Track(
        id = id,
        title = title,
        artistName = artist,
        previewUrl = previewUrl,
        artworkUrl = artworkUrl100
    )
}
