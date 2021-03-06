package com.lizl.mydiary.custom.function

import android.widget.EditText
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jungly.gridpasswordview.GridPasswordView

fun GridPasswordView.getEditText(): EditText?
{
    val viewCount = this.childCount
    for (i in 0 until viewCount)
    {
        val view = this.getChildAt(i)
        if (view is EditText)
        {
            return view
        }
    }
    return null
}
