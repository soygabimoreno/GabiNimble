package soy.gabimoreno.gabinimble.domain

import kotlin.math.abs

/**
 * Offset -> Alpha
 * ---------------
 * 0 -> 1
 * 0.125 -> 0.75
 * 0.25 -> 0.5
 * 0.5 -> 0
 * 0.75 -> 0.5
 * 0.875 -> 0.75
 * 1 -> 1
 */
interface OffsetToAlphaCalculator {

    fun Float.offsetToAlpha(): Float =
        (abs(0.5 - this) * 2).toFloat()
}
