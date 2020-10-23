package soy.gabimoreno.gabinimble.coredomain.repository

import soy.gabimoreno.gabinimble.coredomain.Song

interface SongRepository {
    suspend fun getSongs(filename: String): List<Song>
    suspend fun deleteAllSongsFromLocal()
    suspend fun findById(
        filename: String,
        songId: Long
    ): Song
}
