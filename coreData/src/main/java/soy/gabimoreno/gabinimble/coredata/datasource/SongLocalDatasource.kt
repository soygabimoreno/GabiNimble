package soy.gabimoreno.gabinimble.coredata.datasource

import soy.gabimoreno.gabinimble.coredomain.Song

interface SongLocalDatasource {
    suspend fun isEmpty(): Boolean
    suspend fun getSongs(): List<Song>
    suspend fun findById(songId: Long): Song
    suspend fun saveSongs(songs: List<Song>)
    suspend fun deleteAllSongs()
}
