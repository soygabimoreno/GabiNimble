package soy.gabimoreno.gabinimble.domain

import org.junit.Assert.assertTrue
import org.junit.Test

class OffsetToAlphaCalculatorTest : OffsetToAlphaCalculator {

    @Test
    fun `if offset is 0,0 then return 1,0`() =
        assertTrue(1f == 0f.offsetToAlpha())

    @Test
    fun `if offset is 1,0 then return 1,0`() =
        assertTrue(1f == 1f.offsetToAlpha())

    @Test
    fun `if offset is 0,5 then return 0`() =
        assertTrue(0f == 0.5f.offsetToAlpha())

    @Test
    fun `if offset is 0,25 then return 0,5`() =
        assertTrue(0.5f == 0.25f.offsetToAlpha())

    @Test
    fun `if offset is 0,125 then return 0,75`() =
        assertTrue(0.75f == 0.125f.offsetToAlpha())

    @Test
    fun `if offset is 0,875 then return 0,75`() =
        assertTrue(0.75f == 0.875f.offsetToAlpha())
}
