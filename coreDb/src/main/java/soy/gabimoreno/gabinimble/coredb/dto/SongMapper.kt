package soy.gabimoreno.gabinimble.coredb.dto

import soy.gabimoreno.gabinimble.coredb.model.DbSong
import soy.gabimoreno.gabinimble.coredomain.Song

fun Song.toDbSong(): DbSong = DbSong(
    id = id,
    artist = artist,
    filename = filename,
    name = name,
    description = description,
    thumbnailUrl = thumbnailUrl,
    audioUrl = audioUrl
)

fun DbSong.toSong(): Song = Song(
    id = id,
    artist = artist,
    filename = filename,
    name = name,
    description = description,
    thumbnailUrl = thumbnailUrl,
    audioUrl = audioUrl
)
