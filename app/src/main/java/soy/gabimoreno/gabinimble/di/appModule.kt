package soy.gabimoreno.gabinimble.di

import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module
import soy.gabimoreno.gabinimble.R
import soy.gabimoreno.gabinimble.coreinfrastructure.InfrastructureKeys
import soy.gabimoreno.gabinimble.coreinfrastructure.InfrastructureUrl

val appModule = module {
    single(named(InfrastructureKeys.API_KEY)) { androidApplication().getString(R.string.api_key) }
    single(named(InfrastructureUrl.BASE_URL_SONGS.key)) { InfrastructureUrl.BASE_URL_SONGS.value }
}
