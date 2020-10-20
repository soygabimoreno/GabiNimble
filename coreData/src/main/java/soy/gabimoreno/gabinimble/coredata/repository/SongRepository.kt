package soy.gabimoreno.gabinimble.coredata.repository

import soy.gabimoreno.gabinimble.coredata.datasource.SongLocalDatasource
import soy.gabimoreno.gabinimble.coredata.datasource.SongRemoteDatasource
import soy.gabimoreno.gabinimble.coredomain.Song

class SongRepository(
    private val songLocalDatasource: SongLocalDatasource,
    private val songRemoteDatasource: SongRemoteDatasource
) {
    suspend fun getSongs(filename: String): List<Song> {
        if (songLocalDatasource.isEmpty(filename)) {
            val remoteSongs = songRemoteDatasource.getSongs(filename)
            val remoteSongsWithFileName = mutableListOf<Song>()
            remoteSongs.forEach {
                remoteSongsWithFileName.add(it.copy(filename = filename))
            }
            songLocalDatasource.saveSongs(remoteSongsWithFileName)
        }
        return songLocalDatasource.getSongs(filename)
    }

    suspend fun deleteAllSongsFromLocal() {
        songLocalDatasource.deleteAllSongs()
    }

    suspend fun findById(filename: String, songId: Long): Song {
        if (songLocalDatasource.isEmpty(filename)) {
            val remoteSongs = songRemoteDatasource.getSongs(filename)
            songLocalDatasource.saveSongs(remoteSongs)
        }
        return songLocalDatasource.findById(filename, songId)
    }
}
