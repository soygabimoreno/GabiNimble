package soy.gabimoreno.gabinimble.presentation.main.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
import soy.gabimoreno.gabinimble.coredata.usecase.DeleteAllSongsFromLocalUseCase
import soy.gabimoreno.gabinimble.coredata.usecase.GetSongsUseCase
import soy.gabimoreno.gabinimble.coredomain.Song

@ExperimentalCoroutinesApi
class MainListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val testCoroutineScope = TestCoroutineScope(testDispatcher)

    private val getSongsUseCase = mockk<GetSongsUseCase>()
    private val deleteAllSongsFromLocalUseCase = mockk<DeleteAllSongsFromLocalUseCase>(relaxed = true)

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
        givenRightSongsRetrieved()

        buildViewModel()

        coVerify(exactly = 1) { getSongsUseCase() }
    }

    @Test
    fun `at start, the content is properly shown`() {
        givenRightSongsRetrieved()

        val viewModel = buildViewModel()

        val state = viewModel.viewState.value!!
        assertTrue(state is MainListViewModel.ViewState.Content)
        assertTrue(buildFakeSongs().isEqualTo((state as MainListViewModel.ViewState.Content).courses))
    }

    @Test
    fun `when the user refresh the screen, then the database is cleaned and the content loaded again`() {
        givenRightSongsRetrieved()
        val viewModel = buildViewModel()

        coVerify(exactly = 1) { getSongsUseCase() }

        viewModel.handleCleanDbAndLoadContentAgain()

        coVerify(exactly = 1) { deleteAllSongsFromLocalUseCase() }
        coVerify(exactly = 2) { getSongsUseCase() }
    }

    @Test
    fun `if the web fab is clicked, then its view event is triggered with the right url`() {
        givenRightSongsRetrieved()
        val viewModel = buildViewModel()

        viewModel.handleFabWebClicked()

        val event = viewModel.viewEvents.poll()
        assertTrue(event is MainListViewModel.ViewEvents.NavigateToWeb)
        assertTrue("http://gabinimble.com" == (event as MainListViewModel.ViewEvents.NavigateToWeb).uriString)
    }

    @Test
    fun `if the search fab is clicked, then its view event is triggered`() {
        givenRightSongsRetrieved()
        val viewModel = buildViewModel()

        viewModel.handleFabSearchClicked()

        val event = viewModel.viewEvents.poll()
        assertTrue(event is MainListViewModel.ViewEvents.SearchClicked)
    }

    @Test
    fun `when a song is selected, then navigate to its detailed screen`() {
        givenRightSongsRetrieved()
        val viewModel = buildViewModel()

        val song = buildFakeSong()
        viewModel.handleSongClicked(song)

        val event = viewModel.viewEvents.poll()
        assertTrue(event is MainListViewModel.ViewEvents.NavigateToMainDetail)
        assertTrue(song == (event as MainListViewModel.ViewEvents.NavigateToMainDetail).song)
    }

    private fun givenRightSongsRetrieved() {
        coEvery { getSongsUseCase() } returns buildFakeSongs()
    }

    private fun buildViewModel() = MainListViewModel(
        getSongsUseCase = getSongsUseCase,
        deleteAllSongsFromLocalUseCase = deleteAllSongsFromLocalUseCase
    )

    private fun buildFakeSong() = Song(
        id = 1,
        artist = "GABI NIMBLE",
        name = "Awesome Title",
        description = "1. WAND - Happiness",
        thumbnailUrl = "https://foo.com",
        audioUrl = "https://foo.com"
    )

    private fun buildFakeSongs() = listOf(
        buildFakeSong().copy(id = 2),
        buildFakeSong().copy(id = 3),
        buildFakeSong().copy(id = 4),
        buildFakeSong().copy(id = 5),
        buildFakeSong().copy(id = 6),
        buildFakeSong().copy(id = 7),
    )

    private fun List<Song>.isEqualTo(songs: List<Song>): Boolean {
        if (size != songs.size) {
            return false
        }
        forEachIndexed { index, value ->
            if (songs[index] != value) {
                return false
            }
        }
        return true
    }
}