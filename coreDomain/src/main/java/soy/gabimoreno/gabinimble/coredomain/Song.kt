package soy.gabimoreno.gabinimble.coredomain

import java.io.Serializable

data class Song(
    val id: Long,
    val artist: String,
    val filename: String,
    val name: String,
    val description: String,
    val thumbnailUrl: String,
    val audioUrl: String,
) : Serializable
