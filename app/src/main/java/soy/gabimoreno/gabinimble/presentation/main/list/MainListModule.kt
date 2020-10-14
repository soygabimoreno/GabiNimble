package soy.gabimoreno.gabinimble.presentation.main.list

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mainListModule = module {
    scope(named<MainListFragment>()) {
        viewModel {
            MainListViewModel(
                getSongsUseCase = get(),
                deleteAllSongsFromLocalUseCase = get()
            )
        }
    }
}
