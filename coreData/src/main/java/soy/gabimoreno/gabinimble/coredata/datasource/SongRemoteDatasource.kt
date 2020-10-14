package soy.gabimoreno.gabinimble.coredata.datasource

import soy.gabimoreno.gabinimble.coredomain.Song

interface SongRemoteDatasource {
    suspend fun getSongs(): List<Song>
}
