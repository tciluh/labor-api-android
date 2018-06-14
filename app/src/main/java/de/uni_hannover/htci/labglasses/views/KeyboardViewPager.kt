package de.uni_hannover.htci.labglasses.views

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import de.uni_hannover.htci.labglasses.utils.consume

class KeyboardViewPager(ctx: Context, attr: AttributeSet): ViewPager(ctx, attr) {
    interface NavigationDelegate {
        fun onRightDpad() = Unit
        fun onLeftDpad() = Unit
        fun onCenterDpad() = Unit
    }

    var navigationDelegate: NavigationDelegate? = null
    var touchPagingEnabled: Boolean = true

    override fun executeKeyEvent(event: KeyEvent?): Boolean {
        return when(event?.keyCode) {
            KeyEvent.KEYCODE_DPAD_RIGHT -> consume {
                if(event.action == KeyEvent.ACTION_UP){
                    navigationDelegate?.onRightDpad()
                }
            }
            KeyEvent.KEYCODE_DPAD_LEFT -> consume {
                if(event.action == KeyEvent.ACTION_UP) {
                    navigationDelegate?.onLeftDpad()
                }
            }
            KeyEvent.KEYCODE_DPAD_CENTER -> consume {
                if(event.action == KeyEvent.ACTION_UP) {
                    navigationDelegate?.onCenterDpad()
                }
            }
            else -> super.executeKeyEvent(event)
        }
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return touchPagingEnabled && super.onTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return touchPagingEnabled && super.onInterceptTouchEvent(ev)
    }

    override fun canScrollHorizontally(direction: Int): Boolean {
        return touchPagingEnabled && super.canScrollHorizontally(direction)
    }
}