package soy.gabimoreno.gabinimble.presentation.main.list

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import soy.gabimoreno.gabinimble.coredata.usecase.DeleteAllSongsFromLocalUseCase
import soy.gabimoreno.gabinimble.coredata.usecase.GetSongsUseCase
import soy.gabimoreno.gabinimble.coredomain.Category
import soy.gabimoreno.gabinimble.coredomain.Song
import soy.gabimoreno.gabinimble.libbase.viewmodel.BaseViewModel

class MainListViewModel(
    private val getSongsUseCase: GetSongsUseCase,
    private val deleteAllSongsFromLocalUseCase: DeleteAllSongsFromLocalUseCase
) : BaseViewModel<
        MainListViewModel.ViewState,
        MainListViewModel.ViewEvents>() {

    init {
        handleLoadContent()
    }

    private fun handleLoadContent() {
        updateViewState(ViewState.Loading)
        viewModelScope.launch {
            val featuredSongs = getSongsUseCase(Category.Filename.FEATURED.filename)
            val rememberSongs = getSongsUseCase(Category.Filename.REMEMBER.filename)
            val musicaDivertidaSongs = getSongsUseCase(Category.Filename.MUSICA_DIVERTIDA.filename)
            val categories = listOf(
                Category(Category.Filename.FEATURED.filename, featuredSongs),
                Category(Category.Filename.REMEMBER.filename, rememberSongs),
                Category(Category.Filename.MUSICA_DIVERTIDA.filename, musicaDivertidaSongs)
            )
            updateViewState(ViewState.Content(categories))
        }
    }

    fun handleCleanDbAndLoadContentAgain() {
        viewModelScope.launch {
            deleteAllSongsFromLocalUseCase()
            handleLoadContent()
        }
    }

    fun handleFabWebClicked() {
        viewModelScope.launch {
            sendViewEvent(ViewEvents.NavigateToWeb("http://gabinimble.com"))
        }
    }

    fun handleFabSearchClicked() {
        viewModelScope.launch {
            sendViewEvent(ViewEvents.SearchClicked)
        }
    }

    fun handleSongClicked(filename: String, song: Song) {
        viewModelScope.launch {
            sendViewEvent(ViewEvents.NavigateToMainDetail(filename, song))
        }
    }

    sealed class ViewState {
        object Loading : ViewState()
        object Error : ViewState()
        data class Content(val categories: List<Category>) : ViewState()
    }

    sealed class ViewEvents {
        data class NavigateToWeb(val uriString: String) : ViewEvents()
        object SearchClicked : ViewEvents()
        data class NavigateToMainDetail(
            val filename: String,
            val song: Song
        ) : ViewEvents()
    }
}
