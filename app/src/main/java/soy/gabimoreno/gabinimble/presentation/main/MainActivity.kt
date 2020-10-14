package soy.gabimoreno.gabinimble.presentation.main

import android.content.Context
import android.content.Intent
import android.view.Gravity
import androidx.transition.Slide
import org.koin.androidx.viewmodel.ext.android.viewModel
import soy.gabimoreno.gabinimble.R
import soy.gabimoreno.gabinimble.coredomain.Song
import soy.gabimoreno.gabinimble.libbase.activity.StatelessBaseActivity
import soy.gabimoreno.gabinimble.libframework.extension.exhaustive
import soy.gabimoreno.gabinimble.libframework.extension.navigateAddingToBackStackTo
import soy.gabimoreno.gabinimble.libframework.extension.navigateTo
import soy.gabimoreno.gabinimble.presentation.main.detail.MainDetailFragment
import soy.gabimoreno.gabinimble.presentation.main.list.MainListFragment

class MainActivity : StatelessBaseActivity<
        MainViewModel.ViewEvents,
        MainViewModel
        >() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    override val layoutResId = R.layout.activity_main
    override val viewModel: MainViewModel by viewModel()

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else super.onBackPressed()
    }

    override fun initUI() {}

    override fun handleViewEvent(viewEvent: MainViewModel.ViewEvents) {
        when (viewEvent) {
            is MainViewModel.ViewEvents.NavigateToMainList -> navigateToMainList()
        }.exhaustive
    }

    private fun navigateToMainList() {
        navigateTo(
            R.id.flContainer,
            MainListFragment.newInstance(
                ::navigateToCourseDetail
            ).apply {
                enterTransition = Slide(Gravity.END)
                exitTransition = Slide(Gravity.START)
            }
        )
    }

    private fun navigateToCourseDetail(song: Song) {
        val songId = song.id
        navigateAddingToBackStackTo(
            R.id.flContainer,
            MainDetailFragment.newInstance(songId).apply {
                enterTransition = Slide(Gravity.END)
                exitTransition = Slide(Gravity.START)
            }
        )
    }
}
