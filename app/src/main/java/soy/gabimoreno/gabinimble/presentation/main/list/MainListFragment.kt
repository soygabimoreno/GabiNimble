package soy.gabimoreno.gabinimble.presentation.main.list

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.fragment_main_list.*
import kotlinx.android.synthetic.main.layout_songs_bottom.*
import kotlinx.android.synthetic.main.layout_songs_top.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import soy.gabimoreno.gabinimble.R
import soy.gabimoreno.gabinimble.coredomain.Song
import soy.gabimoreno.gabinimble.domain.OffsetToAlphaCalculator
import soy.gabimoreno.gabinimble.libbase.fragment.BaseFragment
import soy.gabimoreno.gabinimble.libbase.recyclerview.createListAdapter
import soy.gabimoreno.gabinimble.libframework.extension.debugToast
import soy.gabimoreno.gabinimble.libframework.extension.exhaustive
import soy.gabimoreno.gabinimble.libframework.extension.toast
import soy.gabimoreno.gabinimble.presentation.main.list.viewholder.SongsBottomViewHolder
import soy.gabimoreno.gabinimble.presentation.main.list.viewholder.SongsTopViewHolder

class MainListFragment : BaseFragment<
        MainListViewModel.ViewState,
        MainListViewModel.ViewEvents,
        MainListViewModel
        >(), OffsetToAlphaCalculator {

    companion object {
        fun newInstance(
            navigateToSongDetail: (Song) -> Unit
        ) = MainListFragment().apply {
            this.navigateToSongDetail = navigateToSongDetail
        }
    }

    private lateinit var navigateToSongDetail: (Song) -> Unit

    override val layoutResId = R.layout.fragment_main_list
    override val viewModel: MainListViewModel by viewModel()

    private val songsTopListAdapter: ListAdapter<Song, SongsTopViewHolder> by lazy {
        createListAdapter {
            layout { R.layout.item_song_top }

            compareItemsByReference { oldItem, newItem -> oldItem.id == newItem.id }
            compareItemsByContent { oldItem, newItem -> oldItem == newItem }

            viewHolderCreation { view, _ ->
                SongsTopViewHolder(view) { course ->
                    viewModel.handleSongClicked(course)
                }
            }
        }
    }

    private val songsBottomListAdapter: ListAdapter<Song, SongsBottomViewHolder> by lazy {
        createListAdapter {
            layout { R.layout.item_song_bottom }

            compareItemsByReference { oldItem, newItem -> oldItem.id == newItem.id }
            compareItemsByContent { oldItem, newItem -> oldItem == newItem }

            viewHolderCreation { view, _ ->
                SongsBottomViewHolder(view) { course ->
                    viewModel.handleSongClicked(course)
                }
            }
        }
    }

    override fun initUI() {
        initSwipeRefreshLayout()
        initFabs()
        initPurchasedMain()
        initSongsBottom()
    }

    private fun initSwipeRefreshLayout() {
        srl.setOnRefreshListener {
            viewModel.handleCleanDbAndLoadContentAgain()
        }
    }

    private fun initFabs() {
        fabGabiNimble.setOnClickListener {
            viewModel.handleFabGabiNimbleClicked()
        }
        fabSearch.setOnClickListener {
            viewModel.handleFabSearchClicked()
        }
    }

    private fun initPurchasedMain() {
        vpSongsTop.adapter = songsTopListAdapter
        vpSongsTop.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    setPurchasedSongTitle(position)
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    tvNameTop.alpha = positionOffset.offsetToAlpha()
                    if (positionOffset > 0.5) {
                        setPurchasedSongTitle(position + 1)
                    } else {
                        setPurchasedSongTitle(position)
                    }
                }
            })
        btnListenSong.setOnClickListener {
            val position = vpSongsTop.currentItem
            val currentPurchasedSong = songsTopListAdapter.currentList[position]
            viewModel.handleSongClicked(currentPurchasedSong)
        }
    }

    private fun setPurchasedSongTitle(position: Int) {
        val currentPurchasedSong = songsTopListAdapter.currentList[position]
        with(currentPurchasedSong) {
            tvNameTop.text = name
        }
    }

    private fun initSongsBottom() {
        rvSongsBottom.adapter = songsBottomListAdapter
        rvSongsBottom.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
    }

    override fun renderViewState(viewState: MainListViewModel.ViewState) {
        when (viewState) {
            MainListViewModel.ViewState.Loading -> showLoading()
            MainListViewModel.ViewState.Error -> showError()
            is MainListViewModel.ViewState.Content -> showContent(viewState.courses)
        }.exhaustive
    }

    private fun showContent(courses: List<Song>) {
        hideLoading()
        val purchasedMain = courses.subList(0, 4)
        songsTopListAdapter.submitList(purchasedMain)
        wdIndicator.setViewPager2(vpSongsTop)

        val popularMain = courses.subList(4, courses.size)
        songsBottomListAdapter.submitList(popularMain)
        rvSongsBottom.scrollToPosition(0)
    }

    private fun showLoading() {
        srl.isRefreshing = true
    }

    private fun hideLoading() {
        srl.isRefreshing = false
    }

    private fun showError() {
        hideLoading()
        debugToast("Error")
    }

    override fun handleViewEvent(viewEvent: MainListViewModel.ViewEvents) {
        when (viewEvent) {
            is MainListViewModel.ViewEvents.NavigateToWeb -> navigateToWeb(viewEvent.uriString)
            MainListViewModel.ViewEvents.SearchClicked -> toast(R.string.search_not_available)
            is MainListViewModel.ViewEvents.NavigateToMainDetail -> navigateToSongDetail(viewEvent.course)
        }.exhaustive
    }

    private fun navigateToWeb(uriString: String) {
        CustomTabsIntent.Builder()
            .setStartAnimations(
                requireContext(),
                R.anim.browser_in_right,
                R.anim.browser_out_left
            )
            .setExitAnimations(
                requireContext(),
                R.anim.browser_in_left,
                R.anim.browser_out_right
            )
            .build()
            .launchUrl(
                requireContext(),
                Uri.parse(uriString)
            )
    }
}
