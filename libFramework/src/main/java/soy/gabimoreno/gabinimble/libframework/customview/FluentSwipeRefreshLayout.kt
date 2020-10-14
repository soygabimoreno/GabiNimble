package soy.gabimoreno.gabinimble.libframework.customview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlin.math.abs

class FluentSwipeRefreshLayout(
    context: Context,
    attrs: AttributeSet?
) : SwipeRefreshLayout(
    context,
    attrs
) {

    private val touchSlop: Int = ViewConfiguration.get(context).scaledTouchSlop
    private var lastX = 0f

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> lastX = MotionEvent.obtain(event).x
            MotionEvent.ACTION_MOVE -> {
                val eventX = event.x
                val xDiff = abs(eventX - lastX)
                if (xDiff > touchSlop) {
                    return false
                }
            }
        }
        return super.onInterceptTouchEvent(event)
    }
}
