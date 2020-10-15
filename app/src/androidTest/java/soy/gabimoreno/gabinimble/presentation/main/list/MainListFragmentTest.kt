package soy.gabimoreno.gabinimble.presentation.main.list

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.IdlingRegistry
import androidx.test.rule.ActivityTestRule
import com.jakewharton.espresso.OkHttp3IdlingResource
import com.schibsted.spain.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaListInteractions.clickListItem
import com.schibsted.spain.barista.interaction.BaristaViewPagerInteractions.swipeViewPagerForward
import okhttp3.mockwebserver.MockResponse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.get
import soy.gabimoreno.gabinimble.MockWebServerRule
import soy.gabimoreno.gabinimble.R
import soy.gabimoreno.gabinimble.corenetwork.client.SongApiClient
import soy.gabimoreno.gabinimble.fromJson
import soy.gabimoreno.gabinimble.presentation.main.MainActivity

class MainListFragmentTest : KoinTest {

    @get:Rule
    val mockWebServerRule = MockWebServerRule()

    @get:Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java, false, false)

    @Before
    fun setUp() {
        activityTestRule.launchActivity(null)
        mockWebServerRule.server.enqueue(
            MockResponse().fromJson(
                ApplicationProvider.getApplicationContext(),
                "songs.json"
            )
        )

        val resource = OkHttp3IdlingResource.create("OkHttp", get<SongApiClient>().okHttpClient)
        IdlingRegistry.getInstance().register(resource)
    }

    @Test
    fun bottom_carousel_has_the_right_number_of_elements() =
        assertRecyclerViewItemCount(R.id.rvSongsBottom, 19)

    @Test
    fun assert_top_carousel_titles_are_shown_properly() {
        assertDisplayed(R.id.tvNameTop, "94 98")

        swipeViewPagerForward(R.id.vpSongsTop)
        assertDisplayed(R.id.tvNameTop, "93 95")
    }

    @Test
    fun when_user_clicks_one_item_of_the_bottom_carousel_then_detail_screen_shows_the_right_song() {
        clickListItem(R.id.rvSongsBottom, 0)

        assertDisplayed(R.id.tvName, "94 99")
    }

    @Test
    fun when_user_clicks_the_listen_song_button_then_detail_screen_shows_the_right_song() {
        clickOn(R.id.btnListenSong)

        assertDisplayed(R.id.tvName, "94 98")
    }

    @Test
    fun when_user_clicks_one_item_of_the_top_carousel_then_detail_screen_shows_the_right_song() {
        clickOn(R.id.vpSongsTop)

        assertDisplayed(R.id.tvName, "94 98")
    }
}
