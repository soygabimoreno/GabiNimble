package soy.gabimoreno.gabinimble.presentation.main.detail

import android.content.Intent
import android.net.Uri
import kotlinx.android.synthetic.main.fragment_main_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import soy.gabimoreno.gabinimble.BuildConfig
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
        private const val ARG_SONG_FILENAME = "ARG_SONG_FILENAME"
        private const val ARG_SONG_ID = "ARG_SONG_ID"

        fun newInstance(
            filename: String,
            songId: Long,
        ) = MainDetailFragment().withArgs {
            putString(ARG_SONG_FILENAME, filename)
            putLong(ARG_SONG_ID, songId)
        }
    }

    override val layoutResId = R.layout.fragment_main_detail
    override val viewModel: MainDetailViewModel by viewModel() {
        val filename = requireArguments().getString(ARG_SONG_FILENAME)!!
        val songId = requireArguments().getLong(ARG_SONG_ID)
        val params = MainDetailViewModel.Params(
            filename = filename,
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
        ibSendEmail.setOnClickListener {
            viewModel.handleSendEmailClicked()
        }
        ibRate.setOnClickListener {
            viewModel.handleRateClicked()
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
            MainDetailViewModel.ViewEvents.Share -> share()
            MainDetailViewModel.ViewEvents.SendEmail -> sendEmail()
            MainDetailViewModel.ViewEvents.Rate -> rate()
            is MainDetailViewModel.ViewEvents.PlayPlayer -> playPlayer(viewEvent.song)
            MainDetailViewModel.ViewEvents.StopPlayer -> stopPlayer()
        }.exhaustive
    }

    private fun share() {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, getString(R.string.share_song_text))
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun sendEmail() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email_to)))
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
        startActivity(Intent.createChooser(intent, getString(R.string.email_title)))
    }

    private fun rate() {
        val appPackageName = if (BuildConfig.DEBUG) "com.appacoustic.rt" else requireContext().packageName
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
        } catch (exception: Exception) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
        }
    }

    private fun playPlayer(song: Song) {
        PlayerService.start(
            requireContext(),
            song
        )
    }

    private fun stopPlayer() {
        PlayerService.stop(requireContext())
    }
}
