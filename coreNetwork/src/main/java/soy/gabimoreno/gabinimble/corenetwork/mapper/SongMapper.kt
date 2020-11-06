package soy.gabimoreno.gabinimble.corenetwork.mapper

import soy.gabimoreno.gabinimble.coredomain.Song
import soy.gabimoreno.gabinimble.corenetwork.dto.NetworkSong

fun Song.toNetworkSong() = NetworkSong(
    id = id,
    artist = artist,
    filename = filename,
    name = name,
    description = description,
    thumbnailUrl = thumbnailUrl,
    audioUrl = audioUrl
)

fun NetworkSong.toSong() = Song(
    id = id,
    artist = artist,
    filename = filename,
    name = name,
    description = description,
    thumbnailUrl = thumbnailUrl,
    audioUrl = audioUrl
)
