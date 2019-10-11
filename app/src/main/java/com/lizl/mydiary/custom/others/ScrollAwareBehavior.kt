package com.lizl.mydiary.custom.others

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListener
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ScrollAwareBehavior(context: Context, attrs: AttributeSet) : FloatingActionButton.Behavior()
{
    private val INTERPOLATOR = FastOutSlowInInterpolator()
    private var mIsAnimatingOut = false

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, directTargetChild: View, target: View,
            nestedScrollAxes: Int): Boolean
    {
        // Ensure we react to vertical scrolling
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(
                coordinatorLayout, child, directTargetChild, target, nestedScrollAxes
        )
    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, target: View, dxConsumed: Int, dyConsumed: Int,
            dxUnconsumed: Int, dyUnconsumed: Int)
    {
        Log.d("ScrollAwareBehavior", "onNestedScroll: dyConsumed/$dyConsumed")
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
        if (dyConsumed > 0 && !this.mIsAnimatingOut && child.visibility == View.VISIBLE)
        {
            // User scrolled down and the FAB is currently visible -> hide the FAB
            animateOut(child)
        }
        else if (dyConsumed < 0 && child.visibility != View.VISIBLE)
        {
            // User scrolled up and the FAB is currently not visible -> show the FAB
            animateIn(child)
        }
    }

    // Same animation that FloatingActionButton.Behavior uses to hide the FAB when the AppBarLayout exits
    private fun animateOut(button: FloatingActionButton)
    {
        ViewCompat.animate(button).scaleX(0.0f).scaleY(0.0f).alpha(0.0f).setInterpolator(INTERPOLATOR).withLayer()
            .setListener(object : ViewPropertyAnimatorListener
            {
                override fun onAnimationStart(view: View)
                {
                    this@ScrollAwareBehavior.mIsAnimatingOut = true
                }

                override fun onAnimationCancel(view: View)
                {
                    this@ScrollAwareBehavior.mIsAnimatingOut = false
                }

                override fun onAnimationEnd(view: View)
                {
                    this@ScrollAwareBehavior.mIsAnimatingOut = false
                    view.visibility = View.INVISIBLE
                }
            }).start()
    }

    // Same animation that FloatingActionButton.Behavior uses to show the FAB when the AppBarLayout enters
    private fun animateIn(button: FloatingActionButton)
    {
        ViewCompat.animate(button).scaleX(1.0f).scaleY(1.0f).alpha(1.0f).setInterpolator(INTERPOLATOR).withLayer()
            .setListener(object : ViewPropertyAnimatorListener
            {
                override fun onAnimationEnd(view: View)
                {
                }

                override fun onAnimationCancel(view: View)
                {
                }

                override fun onAnimationStart(view: View)
                {
                    view.visibility = View.VISIBLE
                }
            }).start()
    }
}