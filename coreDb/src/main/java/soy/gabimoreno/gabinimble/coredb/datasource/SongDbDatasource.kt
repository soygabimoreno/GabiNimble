package soy.gabimoreno.gabinimble.coredb.datasource

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import soy.gabimoreno.gabinimble.coredata.datasource.SongLocalDatasource
import soy.gabimoreno.gabinimble.coredb.db.SongDb
import soy.gabimoreno.gabinimble.coredb.dto.toDbSong
import soy.gabimoreno.gabinimble.coredb.dto.toSong
import soy.gabimoreno.gabinimble.coredb.model.DbSong
import soy.gabimoreno.gabinimble.coredomain.Song

class SongDbDatasource(songDb: SongDb) : SongLocalDatasource {

    private val songDao = songDb.songDao()

    override suspend fun isEmpty(): Boolean = withContext(Dispatchers.IO) {
        songDao.songCount() <= 0
    }

    override suspend fun getSongs(): List<Song> = withContext(Dispatchers.IO) {
        songDao.getAll().map(DbSong::toSong)
    }

    override suspend fun findById(songId: Long): Song = withContext(Dispatchers.IO) {
        songDao.findById(songId).toSong()
    }

    override suspend fun saveSongs(songs: List<Song>): Unit = withContext(Dispatchers.IO) {
        songDao.insert(songs.map(Song::toDbSong))
    }

    override suspend fun deleteAllSongs(): Unit = withContext(Dispatchers.IO) {
        songDao.deleteAll()
    }
}
