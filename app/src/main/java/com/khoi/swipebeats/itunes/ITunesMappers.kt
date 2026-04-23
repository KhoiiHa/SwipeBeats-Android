package com.khoi.swipebeats.itunes

import com.khoi.swipebeats.explore.Track

fun ITunesTrackDto.toTrack(): Track {
    return Track(
        id = trackId,
        title = trackName,
        artistName = artistName,
        previewUrl = previewUrl,
        artworkUrl = artworkUrl100?.replace("100x100bb.jpg", "600x600bb.jpg")
    )
}
