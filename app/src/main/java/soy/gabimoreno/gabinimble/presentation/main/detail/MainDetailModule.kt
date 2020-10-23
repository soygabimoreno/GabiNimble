package soy.gabimoreno.gabinimble.presentation.main.detail

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import soy.gabimoreno.gabinimble.coredomain.usecase.FindSongByIdUseCase
import soy.gabimoreno.gabinimble.libplayer.Player

val mainDetailModule = module {
    single { Player(androidContext()) }
    single {
        FindSongByIdUseCase(
            songRepository = get()
        )
    }
    scope(named<MainDetailFragment>()) {
        viewModel { (params: MainDetailViewModel.Params) ->
            MainDetailViewModel(
                findSongByIdUseCase = get(),
                player = get(),
                params = params
            )
        }
    }
}
