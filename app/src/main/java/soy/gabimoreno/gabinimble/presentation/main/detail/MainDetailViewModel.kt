package soy.gabimoreno.gabinimble.presentation.main.detail

import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.ui.StyledPlayerView
import kotlinx.coroutines.launch
import soy.gabimoreno.gabinimble.coredata.usecase.FindSongByIdUseCase
import soy.gabimoreno.gabinimble.coredomain.Song
import soy.gabimoreno.gabinimble.libbase.viewmodel.BaseViewModel
import soy.gabimoreno.gabinimble.libplayer.Player

class MainDetailViewModel(
    private val findSongByIdUseCase: FindSongByIdUseCase,
    private val player: Player,
    params: Params
) : BaseViewModel<
        MainDetailViewModel.ViewState,
        MainDetailViewModel.ViewEvents>() {

    data class Params(
        val songId: Long
    )

    private val songId = params.songId

    init {
        handleLoadContent()
    }

    private fun handleLoadContent() {
        updateViewState(ViewState.Loading)
        viewModelScope.launch {
            val song = findSongByIdUseCase(songId)
            updateViewState(ViewState.Content(song))
        }
    }

    fun handleBackClicked() {
        viewModelScope.launch {
            sendViewEvent(ViewEvents.NavigateToBack)
        }
    }

    fun handleShareClicked() {
        viewModelScope.launch {
            val state = getViewState()
            if (state is ViewState.Content) {
                val song = state.song
                sendViewEvent(ViewEvents.ShareSong(song))
            }
        }
    }

    fun initPlayer(pv: StyledPlayerView, song: Song) {
        val audioUrl = song.audioUrl
        player.init(
            exoplayerView = pv,
            uriStrings = listOf(audioUrl)
        )
    }

    fun handleOnResume() {
        player.play()
    }

    fun handleOnPause() {
        player.pause()
    }

    fun handleOnDestroy() {
        player.release()
    }

    sealed class ViewState {
        object Loading : ViewState()
        object Error : ViewState()
        data class Content(val song: Song) : ViewState()
    }

    sealed class ViewEvents {
        data class ShareSong(val song: Song) : ViewEvents()
        object NavigateToBack : ViewEvents()
    }
}