package soy.gabimoreno.gabinimble.coredb.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DbSong(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val artist: String,
    val filename: String,
    val name: String,
    val description: String,
    val thumbnailUrl: String,
    val audioUrl: String,
)
