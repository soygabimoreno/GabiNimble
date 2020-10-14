package soy.gabimoreno.gabinimble.coredata.repository

import soy.gabimoreno.gabinimble.coredata.datasource.SongLocalDatasource
import soy.gabimoreno.gabinimble.coredata.datasource.SongRemoteDatasource
import soy.gabimoreno.gabinimble.coredomain.Song

class SongRepository(
    private val songLocalDatasource: SongLocalDatasource,
    private val songRemoteDatasource: SongRemoteDatasource
) {
    suspend fun getSongs(): List<Song> {
        if (songLocalDatasource.isEmpty()) {
            val remoteSongs = songRemoteDatasource.getSongs()
            songLocalDatasource.saveSongs(remoteSongs)
        }
        return songLocalDatasource.getSongs()
    }

    suspend fun deleteAllSongsFromLocal(): List<Song> {
        songLocalDatasource.deleteAllSongs()
        return songLocalDatasource.getSongs()
    }

    suspend fun findById(songId: Long): Song {
        if (songLocalDatasource.isEmpty()) {
            val remoteSongs = songRemoteDatasource.getSongs()
            songLocalDatasource.saveSongs(remoteSongs)
        }
        return songLocalDatasource.findById(songId)
    }
}
