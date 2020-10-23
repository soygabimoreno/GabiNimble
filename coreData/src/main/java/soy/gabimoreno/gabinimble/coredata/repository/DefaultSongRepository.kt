package soy.gabimoreno.gabinimble.coredata.repository

import soy.gabimoreno.gabinimble.coredata.datasource.SongLocalDatasource
import soy.gabimoreno.gabinimble.coredata.datasource.SongRemoteDatasource
import soy.gabimoreno.gabinimble.coredomain.Song
import soy.gabimoreno.gabinimble.coredomain.repository.SongRepository

class DefaultSongRepository(
    private val songLocalDatasource: SongLocalDatasource,
    private val songRemoteDatasource: SongRemoteDatasource
) : SongRepository {
    override suspend fun getSongs(filename: String): List<Song> {
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

    override suspend fun deleteAllSongsFromLocal() {
        songLocalDatasource.deleteAllSongs()
    }

    override suspend fun findById(
        filename: String,
        songId: Long
    ): Song {
        if (songLocalDatasource.isEmpty(filename)) {
            val remoteSongs = songRemoteDatasource.getSongs(filename)
            songLocalDatasource.saveSongs(remoteSongs)
        }
        return songLocalDatasource.findById(
            filename,
            songId
        )
    }
}
