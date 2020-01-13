package com.lizl.mydiary.custom.view

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView

class EmptyClickRecyclerView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : RecyclerView(context, attrs, defStyleAttr)
{
    private var onEmptyClickListener: (() -> Unit)? = null
    private var onEmptyLongClickListener: (() -> Unit)? = null

    private var lastTouchTime = 0L

    private val mTouchFrame: Rect by lazy { Rect() }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean
    {
        when (ev.action)
        {
            MotionEvent.ACTION_DOWN   ->
            {
                if (pointToPosition(ev.x.toInt(), ev.y.toInt()) == -1)
                {
                    lastTouchTime = System.currentTimeMillis()
                    resetLongClick()
                }
            }
            MotionEvent.ACTION_CANCEL ->
            {
                lastTouchTime = 0
                cancelLongClick()
            }
            MotionEvent.ACTION_UP     ->
            {
                if (System.currentTimeMillis() - lastTouchTime < 300)
                {
                    onEmptyClickListener?.invoke()
                }
                lastTouchTime = 0
                cancelLongClick()
            }
            else                      -> lastTouchTime = 0
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun pointToPosition(x: Int, y: Int): Int
    {
        val count = childCount
        for (i in count - 1 downTo 0)
        {
            val child = getChildAt(i)
            if (child.isVisible)
            {
                child.getHitRect(mTouchFrame)
                if (mTouchFrame.contains(x, y))
                {
                    return i
                }
            }
        }
        return -1
    }

    private fun resetLongClick()
    {
        removeCallbacks(longClickListener)
        postDelayed(longClickListener, 600)
    }

    private fun cancelLongClick()
    {
        removeCallbacks(longClickListener)
    }

    fun setOnEmptyClickListener(onEmptyClickListener: () -> Unit)
    {
        this.onEmptyClickListener = onEmptyClickListener
    }

    fun setOnEmptyLongClickListener(onEmptyLongClickListener: () -> Unit)
    {
        this.onEmptyLongClickListener = onEmptyLongClickListener
    }

    private val longClickListener = Runnable { onEmptyLongClickListener?.invoke() }

}