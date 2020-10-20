package soy.gabimoreno.gabinimble.coredomain

import java.io.Serializable

data class Category(
    val filename: String,
    val songs: List<Song>
) : Serializable {

    enum class Filename(val filename: String) {
        FEATURED("featured"),
        REMEMBER("remember"),
        MUSICA_DIVERTIDA("musica-divertida")
    }
}
