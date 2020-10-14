package soy.gabimoreno.gabinimble.corenetwork.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import soy.gabimoreno.gabinimble.coredata.datasource.SongRemoteDatasource
import soy.gabimoreno.gabinimble.coreinfrastructure.InfrastructureUrl
import soy.gabimoreno.gabinimble.corenetwork.client.SongApiClient
import soy.gabimoreno.gabinimble.corenetwork.datasource.SongNetworkDatasource

val coreNetworkModule = module {
    single { SongApiClient(baseUrl = get(named(InfrastructureUrl.BASE_URL_SONGS.key))) }
    single<SongRemoteDatasource> { SongNetworkDatasource(songApiClient = get()) }
}
