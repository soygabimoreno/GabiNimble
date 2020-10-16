package soy.gabimoreno.gabinimble.presentation.main.detail

import android.content.Intent
import kotlinx.android.synthetic.main.fragment_main_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import soy.gabimoreno.gabinimble.R
import soy.gabimoreno.gabinimble.coredomain.Song
import soy.gabimoreno.gabinimble.domain.OffsetToAlphaCalculator
import soy.gabimoreno.gabinimble.libbase.fragment.BaseFragment
import soy.gabimoreno.gabinimble.libframework.extension.*
import soy.gabimoreno.gabinimble.presentation.main.service.PlayerService

class MainDetailFragment : BaseFragment<
        MainDetailViewModel.ViewState,
        MainDetailViewModel.ViewEvents,
        MainDetailViewModel
        >(), OffsetToAlphaCalculator {

    companion object {
        private const val ARG_SONG_ID = "ARG_SONG_ID"

        fun newInstance(
            songId: Long,
        ) = MainDetailFragment().withArgs {
            putLong(ARG_SONG_ID, songId)
        }
    }

    override val layoutResId = R.layout.fragment_main_detail
    override val viewModel: MainDetailViewModel by viewModel() {
        val songId = requireArguments().getLong(ARG_SONG_ID)
        val params = MainDetailViewModel.Params(
            songId = songId
        )
        parametersOf(params)
    }

    override fun onResume() {
        super.onResume()
        viewModel.handleOnResume()
    }

    override fun initUI() {
        initTopButtons()
    }

    private fun initTopButtons() {
        ibBack.setOnClickListener {
            viewModel.handleBackClicked()
        }
        ibShare.setOnClickListener {
            viewModel.handleShareClicked()
        }
    }

    override fun renderViewState(viewState: MainDetailViewModel.ViewState) {
        when (viewState) {
            MainDetailViewModel.ViewState.Loading -> showLoading()
            MainDetailViewModel.ViewState.Error -> showError()
            is MainDetailViewModel.ViewState.Content -> showContent(viewState.song)
        }.exhaustive
    }

    private fun showContent(song: Song) {
        hideLoading()
        with(song) {
            viewModel.initPlayer(pv, this)
            tvName.text = name
            tvDescription.text = description
        }
    }

    private fun showLoading() {
        pb.visible()
    }

    private fun hideLoading() {
        pb.gone()
    }

    private fun showError() {
        hideLoading()
        debugToast("Error")
    }

    override fun handleViewEvent(viewEvent: MainDetailViewModel.ViewEvents) {
        when (viewEvent) {
            MainDetailViewModel.ViewEvents.NavigateToBack -> requireActivity().onBackPressed()
            is MainDetailViewModel.ViewEvents.ShareSong -> shareSong(viewEvent.song)
            is MainDetailViewModel.ViewEvents.PlayPlayer -> playPlayer(viewEvent.song)
            MainDetailViewModel.ViewEvents.StopPlayer -> stopPlayer()
        }.exhaustive
    }

    private fun shareSong(song: Song) {
        with(song) {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, String.format(getString(R.string.share_song_text), name, audioUrl))
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }

    private fun playPlayer(song: Song) {
        val songName = song.name
        val intent = Intent(requireContext(), PlayerService::class.java)
        intent.putExtra(PlayerService.EXTRA_SONG_NAME, songName)
        requireContext().startForegroundService(intent)
    }

    private fun stopPlayer() {
        val intent = Intent(requireContext(), PlayerService::class.java)
        requireContext().stopService(intent)
    }
}
