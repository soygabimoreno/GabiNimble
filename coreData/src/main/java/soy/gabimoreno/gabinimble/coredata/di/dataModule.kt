package soy.gabimoreno.gabinimble.coredata.di

import org.koin.dsl.module
import soy.gabimoreno.gabinimble.coredata.repository.DefaultSongRepository
import soy.gabimoreno.gabinimble.coredomain.repository.SongRepository
import soy.gabimoreno.gabinimble.coredomain.usecase.DeleteAllSongsFromLocalUseCase
import soy.gabimoreno.gabinimble.coredomain.usecase.GetSongsUseCase

val coreDataModule = module {
    single<SongRepository> {
        DefaultSongRepository(
            songLocalDatasource = get(),
            songRemoteDatasource = get()
        )
    }
    single { GetSongsUseCase(songRepository = get()) }
    single { DeleteAllSongsFromLocalUseCase(songRepository = get()) }
}
