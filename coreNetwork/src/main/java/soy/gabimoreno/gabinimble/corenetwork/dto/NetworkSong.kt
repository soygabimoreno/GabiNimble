package soy.gabimoreno.gabinimble.corenetwork.dto

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class NetworkSong(
    val id: Long,
    val artist: String,
    val filename: String = "",
    val name: String,
    val description: String,
    val thumbnailUrl: String,
    val audioUrl: String,
) : Serializable
