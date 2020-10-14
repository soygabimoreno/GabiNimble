package soy.gabimoreno.gabinimble.presentation.main.list

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import soy.gabimoreno.gabinimble.coredata.usecase.DeleteAllSongsFromLocalUseCase
import soy.gabimoreno.gabinimble.coredata.usecase.GetSongsUseCase
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
            val courses = getSongsUseCase()
            updateViewState(ViewState.Content(courses))
        }
    }

    fun handleCleanDbAndLoadContentAgain() {
        viewModelScope.launch {
            deleteAllSongsFromLocalUseCase()
            handleLoadContent()
        }
    }

    fun handleFabGabiNimbleClicked() {
        viewModelScope.launch {
            sendViewEvent(ViewEvents.NavigateToWeb("http://gabinimble.com"))
        }
    }

    fun handleFabSearchClicked() {
        viewModelScope.launch {
            sendViewEvent(ViewEvents.SearchClicked)
        }
    }

    fun handleSongClicked(song: Song) {
        viewModelScope.launch {
            sendViewEvent(ViewEvents.NavigateToMainDetail(song))
        }
    }

    sealed class ViewState {
        object Loading : ViewState()
        object Error : ViewState()
        data class Content(val courses: List<Song>) : ViewState()
    }

    sealed class ViewEvents {
        data class NavigateToWeb(val uriString: String) : ViewEvents()
        object SearchClicked : ViewEvents()
        data class NavigateToMainDetail(val course: Song) : ViewEvents()
    }
}
