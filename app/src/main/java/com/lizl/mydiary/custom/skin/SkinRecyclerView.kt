package com.lizl.mydiary.custom.skin

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import skin.support.widget.SkinCompatBackgroundHelper
import skin.support.widget.SkinCompatSupportable


class SkinRecyclerView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : RecyclerView(context, attrs, defStyleAttr), SkinCompatSupportable
{
    private var mBackgroundTintHelper: SkinCompatBackgroundHelper = SkinCompatBackgroundHelper(this)

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init
    {
        mBackgroundTintHelper.loadFromAttributes(attrs, defStyleAttr)
    }

    override fun setBackgroundResource(@DrawableRes
                                       resId: Int)
    {
        super.setBackgroundResource(resId)
        mBackgroundTintHelper.onSetBackgroundResource(resId)
    }

    override fun applySkin()
    {
        mBackgroundTintHelper.applySkin()
    }
}