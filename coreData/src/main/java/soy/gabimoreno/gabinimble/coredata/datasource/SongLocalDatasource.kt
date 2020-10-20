package soy.gabimoreno.gabinimble.coredata.datasource

import soy.gabimoreno.gabinimble.coredomain.Song

interface SongLocalDatasource {
    suspend fun isEmpty(filename: String): Boolean
    suspend fun getSongs(filename: String): List<Song>
    suspend fun findById(filename: String, songId: Long): Song
    suspend fun saveSongs(songs: List<Song>)
    suspend fun deleteAllSongs()
}
