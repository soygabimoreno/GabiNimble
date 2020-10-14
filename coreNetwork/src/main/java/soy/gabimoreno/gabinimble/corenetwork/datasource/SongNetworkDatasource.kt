package soy.gabimoreno.gabinimble.corenetwork.datasource

import soy.gabimoreno.gabinimble.coredata.datasource.SongRemoteDatasource
import soy.gabimoreno.gabinimble.coredomain.Song
import soy.gabimoreno.gabinimble.corenetwork.client.SongApiClient

class SongNetworkDatasource(
    private val songApiClient: SongApiClient
) : SongRemoteDatasource {

    override suspend fun getSongs(): List<Song> {
        return songApiClient.service
            .getSongs()
    }
}
