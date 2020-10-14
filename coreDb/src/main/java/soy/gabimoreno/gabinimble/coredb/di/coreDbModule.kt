package soy.gabimoreno.gabinimble.coredb.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import soy.gabimoreno.gabinimble.coredata.datasource.SongLocalDatasource
import soy.gabimoreno.gabinimble.coredb.datasource.SongDbDatasource
import soy.gabimoreno.gabinimble.coredb.db.SongDb

val coreDbModule = module {
    single { SongDb.build(androidContext()) }
    single<SongLocalDatasource> { SongDbDatasource(songDb = get()) }
}
