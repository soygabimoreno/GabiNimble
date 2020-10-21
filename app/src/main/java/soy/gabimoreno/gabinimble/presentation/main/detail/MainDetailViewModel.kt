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
        val filename: String,
        val songId: Long
    )

    private val filename = params.filename
    private val songId = params.songId

    init {
        handleLoadContent()
    }

    private fun handleLoadContent() {
        updateViewState(ViewState.Loading)
        viewModelScope.launch {
            val song = findSongByIdUseCase(filename, songId)
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
            sendViewEvent(ViewEvents.Share)
        }
    }

    fun handleSendEmailClicked() {
        viewModelScope.launch {
            sendViewEvent(ViewEvents.SendEmail)
        }
    }

    fun handleRateClicked() {
        viewModelScope.launch {
            sendViewEvent(ViewEvents.Rate)
        }
    }

    fun initPlayer(pv: StyledPlayerView, song: Song) {
        val audioUrl = song.audioUrl
        player.init(
            exoplayerView = pv,
            uriStrings = listOf(audioUrl)
        ) {
            handleStopPlayer()
        }
    }

    fun handleOnResume() {
        viewModelScope.launch {
            val song = findSongByIdUseCase(filename, songId)
            sendViewEvent(ViewEvents.PlayPlayer(song))
        }
    }

    private fun handleStopPlayer() {
        viewModelScope.launch {
            sendViewEvent(ViewEvents.StopPlayer)
        }
    }

    sealed class ViewState {
        object Loading : ViewState()
        object Error : ViewState()
        data class Content(val song: Song) : ViewState()
    }

    sealed class ViewEvents {
        object Share : ViewEvents()
        object SendEmail : ViewEvents()
        object Rate : ViewEvents()
        object NavigateToBack : ViewEvents()
        data class PlayPlayer(val song: Song) : ViewEvents()
        object StopPlayer : ViewEvents()
    }
}
