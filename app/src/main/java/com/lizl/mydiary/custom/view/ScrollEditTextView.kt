package com.lizl.mydiary.custom.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import skin.support.widget.SkinCompatEditText

class ScrollEditTextView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : SkinCompatEditText(context, attrs, defStyleAttr)
{
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private var mOffsetHeight = 0
    private var mBottomFlag = false
    private var scrollEnable = false

    fun setScrollEnable(enable: Boolean)
    {
        scrollEnable = enable
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        mOffsetHeight = layout.height + totalPaddingTop + totalPaddingBottom - height
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean
    {
        if (scrollEnable)
        {
            if (event?.action == MotionEvent.ACTION_DOWN)
            {
                mBottomFlag = false
            }
            if (mBottomFlag)
            {
                event?.action = MotionEvent.ACTION_CANCEL
            }
        }
        return super.dispatchTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean
    {
        val result = super.onTouchEvent(event)
        if (scrollEnable && !mBottomFlag)
        {
            parent.requestDisallowInterceptTouchEvent(true)
        }
        return result
    }

    override fun onScrollChanged(horiz: Int, vert: Int, oldHoriz: Int, oldVert: Int)
    {
        super.onScrollChanged(horiz, vert, oldHoriz, oldVert)
        if (scrollEnable && (vert == mOffsetHeight || vert == 0))
        {
            parent.requestDisallowInterceptTouchEvent(false)
            mBottomFlag = true
        }
    }

}