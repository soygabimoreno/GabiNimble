package soy.gabimoreno.gabinimble.coredomain

data class Song(
    val id: Long,
    val artist: String,
    val name: String,
    val description: String,
    val thumbnailUrl: String,
    val audioUrl: String,
)