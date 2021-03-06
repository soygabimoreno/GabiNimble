package soy.gabimoreno.gabinimble.presentation.main.list

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.viewpager2.widget.ViewPager2
import org.koin.androidx.viewmodel.ext.android.viewModel
import soy.gabimoreno.gabinimble.R
import soy.gabimoreno.gabinimble.coredomain.Category
import soy.gabimoreno.gabinimble.coredomain.Song
import soy.gabimoreno.gabinimble.databinding.FragmentMainListBinding
import soy.gabimoreno.gabinimble.databinding.ItemSongTopBinding
import soy.gabimoreno.gabinimble.domain.OffsetToAlphaCalculator
import soy.gabimoreno.gabinimble.libbase.fragment.BaseFragment
import soy.gabimoreno.gabinimble.libbase.recyclerview.createBindingListAdapter
import soy.gabimoreno.gabinimble.libframework.extension.debugToast
import soy.gabimoreno.gabinimble.libframework.extension.exhaustive
import soy.gabimoreno.gabinimble.libframework.extension.toast
import soy.gabimoreno.gabinimble.presentation.main.list.viewholder.SongsBottomViewHolder
import soy.gabimoreno.gabinimble.presentation.main.list.viewholder.SongsTopViewHolder

class MainListFragment : BaseFragment<
    FragmentMainListBinding,
    MainListViewModel.ViewState,
    MainListViewModel.ViewEvents,
    MainListViewModel
    >(),
    OffsetToAlphaCalculator {

    companion object {
        fun newInstance(
            navigateToSongDetail: (filename: String, song: Song) -> Unit
        ) = MainListFragment().apply {
            this.navigateToSongDetail = navigateToSongDetail
        }
    }

    private lateinit var navigateToSongDetail: (filename: String, song: Song) -> Unit

    override val viewBinding: (LayoutInflater, ViewGroup?) -> FragmentMainListBinding = { layoutInflater, viewGroup ->
        FragmentMainListBinding.inflate(
            layoutInflater,
            viewGroup,
            false
        )
    }

    override val viewModel: MainListViewModel by viewModel()

    private val featuredListAdapter: ListAdapter<Song, SongsTopViewHolder> by lazy {
        createBindingListAdapter {
            holderViewBinding { layoutInflater, viewGroup ->
                ItemSongTopBinding.inflate(
                    layoutInflater,
                    viewGroup,
                    false
                )
            }

            compareItemsByReference { oldItem, newItem -> oldItem.id == newItem.id }
            compareItemsByContent { oldItem, newItem -> oldItem == newItem }

            viewHolderCreation { binding, _ ->
                SongsTopViewHolder(
                    binding = binding
                ) { song ->
                    viewModel.handleSongClicked(
                        Category.Type.FEATURED.filename,
                        song
                    )
                }
            }
        }
    }

    private val rememberListAdapter: ListAdapter<Song, SongsBottomViewHolder> by lazy {
        createBindingListAdapter {
            layout { R.layout.item_song_bottom }

            compareItemsByReference { oldItem, newItem -> oldItem.id == newItem.id }
            compareItemsByContent { oldItem, newItem -> oldItem == newItem }

            viewHolderCreation { view, _ ->
                SongsBottomViewHolder(
                    layoutInflater = layoutInflater,
                    itemView = view
                ) { song ->
                    viewModel.handleSongClicked(
                        Category.Type.REMEMBER.filename,
                        song
                    )
                }
            }
        }
    }

    private val musicaDivertidaListAdapter: ListAdapter<Song, SongsBottomViewHolder> by lazy {
        createBindingListAdapter {
            layout { R.layout.item_song_bottom }

            compareItemsByReference { oldItem, newItem -> oldItem.id == newItem.id }
            compareItemsByContent { oldItem, newItem -> oldItem == newItem }

            viewHolderCreation { view, _ ->
                SongsBottomViewHolder(
                    layoutInflater = layoutInflater,
                    itemView = view
                ) { song ->
                    viewModel.handleSongClicked(
                        Category.Type.MUSICA_DIVERTIDA.filename,
                        song
                    )
                }
            }
        }
    }

    override fun initUI() {
        initSwipeRefreshLayout()
        initFabs()
        initPurchasedMain()
        initRememberSongs()
        initMusicaDivertidaSongs()
    }

    private fun initSwipeRefreshLayout() {
        binding.srl.setOnRefreshListener {
            viewModel.handleCleanDbAndLoadContentAgain()
        }
    }

    private fun initFabs() {
        binding.clSongsTop.fabWeb.setOnClickListener {
            viewModel.handleFabWebClicked()
        }
//        fabSearch.setOnClickListener {
//            viewModel.handleFabSearchClicked()
//        }
    }

    private fun initPurchasedMain() {
        with(binding.clSongsTop) {
            vpSongsTop.adapter = featuredListAdapter
            vpSongsTop.registerOnPageChangeCallback(
                object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        setFeaturedSongTitle(position)
                    }

                    override fun onPageScrolled(
                        position: Int,
                        positionOffset: Float,
                        positionOffsetPixels: Int
                    ) {
                        tvNameTop.alpha = positionOffset.offsetToAlpha()
                        if (positionOffset > 0.5) {
                            setFeaturedSongTitle(position + 1)
                        } else {
                            setFeaturedSongTitle(position)
                        }
                    }
                })
            btnListenSong.setOnClickListener {
                val position = vpSongsTop.currentItem
                val currentPurchasedSong = featuredListAdapter.currentList[position]
                viewModel.handleSongClicked(
                    Category.Type.FEATURED.filename,
                    currentPurchasedSong
                )
            }
        }
    }

    private fun setFeaturedSongTitle(position: Int) {
        val currentPurchasedSong = featuredListAdapter.currentList[position]
        with(currentPurchasedSong) {
            binding.clSongsTop.tvNameTop.text = name
        }
    }

    private fun initRememberSongs() {
        with(binding.clSongsBottom) {
            rvRememberSongs.adapter = rememberListAdapter
            rvRememberSongs.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }
    }

    private fun initMusicaDivertidaSongs() {
        with(binding.clSongsBottom) {
            rvMusicaDivertidaSongs.adapter = musicaDivertidaListAdapter
            rvMusicaDivertidaSongs.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }
    }

    override fun renderViewState(viewState: MainListViewModel.ViewState) {
        when (viewState) {
            MainListViewModel.ViewState.Loading -> showLoading()
            MainListViewModel.ViewState.Error -> showError()
            is MainListViewModel.ViewState.Content -> showContent(viewState.categories)
        }.exhaustive
    }

    private fun showContent(categories: List<Category>) {
        hideLoading()
        val featuredSongs = categories[0].songs
        featuredListAdapter.submitList(featuredSongs)
        binding.clSongsTop.wdIndicator.setViewPager2(binding.clSongsTop.vpSongsTop)

        val rememberSongs = categories[1].songs
        rememberListAdapter.submitList(rememberSongs)
        binding.clSongsBottom.rvRememberSongs.scrollToPosition(0)

        val musicaDivertidaSongs = categories[2].songs
        musicaDivertidaListAdapter.submitList(musicaDivertidaSongs)
        binding.clSongsBottom.rvMusicaDivertidaSongs.scrollToPosition(0)
    }

    private fun showLoading() {
        binding.srl.isRefreshing = true
    }

    private fun hideLoading() {
        binding.srl.isRefreshing = false
    }

    private fun showError() {
        hideLoading()
        debugToast("Error")
    }

    override fun handleViewEvent(viewEvent: MainListViewModel.ViewEvents) {
        when (viewEvent) {
            is MainListViewModel.ViewEvents.NavigateToWeb -> navigateToWeb(viewEvent.uriString)
            MainListViewModel.ViewEvents.SearchClicked -> toast(R.string.search_not_available)
            is MainListViewModel.ViewEvents.NavigateToMainDetail -> navigateToSongDetail(
                viewEvent.filename,
                viewEvent.song
            )
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
