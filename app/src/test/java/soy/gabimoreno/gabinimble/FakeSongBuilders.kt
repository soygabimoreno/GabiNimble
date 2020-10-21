package soy.gabimoreno.gabinimble

import soy.gabimoreno.gabinimble.coredomain.Category
import soy.gabimoreno.gabinimble.coredomain.Song

fun buildFakeSong() = Song(
    id = 1,
    artist = "GABI NIMBLE",
    filename = "foo",
    name = "Awesome Title",
    description = "1. WAND - Happiness",
    thumbnailUrl = "https://foo.com",
    audioUrl = "https://foo.com"
)

fun buildFakeSongs() = listOf(
    buildFakeSong().copy(id = 2),
    buildFakeSong().copy(id = 3),
    buildFakeSong().copy(id = 4),
    buildFakeSong().copy(id = 5),
    buildFakeSong().copy(id = 6),
    buildFakeSong().copy(id = 7),
)

fun buildFakeCategories() = listOf(
    Category("featured", buildFakeSongs()),
    Category("remember", buildFakeSongs()),
    Category("musica-divertida", buildFakeSongs().reversed())
)

fun List<Category>.isEqualTo(categories: List<Category>): Boolean {
    if (size != categories.size) {
        return false
    }
    forEachIndexed { index, value ->
        if (categories[index] != value) {
            return false
        }
    }
    return true
}
