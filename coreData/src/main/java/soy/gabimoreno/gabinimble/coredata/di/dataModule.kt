package soy.gabimoreno.gabinimble.coredata.di

import org.koin.dsl.module
import soy.gabimoreno.gabinimble.coredata.repository.SongRepository
import soy.gabimoreno.gabinimble.coredata.usecase.DeleteAllSongsFromLocalUseCase
import soy.gabimoreno.gabinimble.coredata.usecase.GetSongsUseCase

val coreDataModule = module {
    single {
        SongRepository(
            songLocalDatasource = get(),
            songRemoteDatasource = get()
        )
    }
    single { GetSongsUseCase(songRepository = get()) }
    single { DeleteAllSongsFromLocalUseCase(songRepository = get()) }
}
