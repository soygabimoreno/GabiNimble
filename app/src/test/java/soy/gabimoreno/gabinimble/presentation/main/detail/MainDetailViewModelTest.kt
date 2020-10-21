package soy.gabimoreno.gabinimble.presentation.main.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.exoplayer2.ui.StyledPlayerView
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import soy.gabimoreno.gabinimble.buildFakeSong
import soy.gabimoreno.gabinimble.coredata.usecase.FindSongByIdUseCase
import soy.gabimoreno.gabinimble.libplayer.Player

@ExperimentalCoroutinesApi
class MainDetailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val testCoroutineScope = TestCoroutineScope(testDispatcher)

    private val findSongByIdUseCase = mockk<FindSongByIdUseCase>()
    private val player = mockk<Player>(relaxed = true)

    private val filename = "foo"
    private val songId = 3210L

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
        testCoroutineScope.cleanupTestCoroutines()
    }

    @Test
    fun `at start, the songs are requested`() {
        givenRightSongRetrieved()

        buildViewModel()

        coVerify(exactly = 1) { findSongByIdUseCase(filename, songId) }
    }

    @Test
    fun `at start, the content is properly shown`() {
        givenRightSongRetrieved()

        val viewModel = buildViewModel()

        val state = viewModel.viewState.value!!
        assertTrue(state is MainDetailViewModel.ViewState.Content)
        assertTrue(buildFakeSong() == (state as MainDetailViewModel.ViewState.Content).song)
    }

    @Test
    fun `if the back button is clicked, then its view event is triggered`() {
        givenRightSongRetrieved()
        val viewModel = buildViewModel()

        viewModel.handleBackClicked()

        val event = viewModel.viewEvents.poll()
        assertTrue(event is MainDetailViewModel.ViewEvents.NavigateToBack)
    }

    @Test
    fun `if the share button fab is clicked, then its view event is triggered`() {
        givenRightSongRetrieved()
        val viewModel = buildViewModel()

        viewModel.handleShareClicked()

        val event = viewModel.viewEvents.poll()
        assertTrue(event is MainDetailViewModel.ViewEvents.Share)
    }

    @Test
    fun `if the email button fab is clicked, then its view event is triggered`() {
        givenRightSongRetrieved()
        val viewModel = buildViewModel()

        viewModel.handleSendEmailClicked()

        val event = viewModel.viewEvents.poll()
        assertTrue(event is MainDetailViewModel.ViewEvents.SendEmail)
    }

    @Test
    fun `if the rate button fab is clicked, then its view event is triggered`() {
        givenRightSongRetrieved()
        val viewModel = buildViewModel()

        viewModel.handleRateClicked()

        val event = viewModel.viewEvents.poll()
        assertTrue(event is MainDetailViewModel.ViewEvents.Rate)
    }

    @Test
    fun `when the content is loaded, then the player is called`() {
        givenRightSongRetrieved()
        val viewModel = buildViewModel()

        val pv = mockk<StyledPlayerView>()
        viewModel.initPlayer(pv, buildFakeSong())

        coVerify(exactly = 1) { player.init(any(), any(), any()) }
    }

    private fun givenRightSongRetrieved() {
        coEvery { findSongByIdUseCase(filename, songId) } returns buildFakeSong()
    }

    private fun buildViewModel() = MainDetailViewModel(
        findSongByIdUseCase = findSongByIdUseCase,
        player = player,
        params = MainDetailViewModel.Params(filename, songId)
    )
}
