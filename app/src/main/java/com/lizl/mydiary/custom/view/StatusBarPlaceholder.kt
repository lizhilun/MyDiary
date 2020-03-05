package com.lizl.mydiary.custom.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import com.blankj.utilcode.util.BarUtils
import skin.support.constraint.SkinCompatConstraintLayout

class StatusBarPlaceholder(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : SkinCompatConstraintLayout(context, attrs, defStyleAttr)
{
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init
    {
        initView(context)
    }

    private fun initView(context: Context)
    {
        val statusBarHolder = View(context)
        statusBarHolder.id = generateViewId()
        addView(statusBarHolder)

        val constraintSet = ConstraintSet()
        constraintSet.clone(this)

        constraintSet.constrainHeight(statusBarHolder.id, BarUtils.getStatusBarHeight())
        constraintSet.constrainWidth(statusBarHolder.id, LayoutParams.MATCH_PARENT)
        constraintSet.connect(statusBarHolder.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)

        constraintSet.applyTo(this)
    }
}