package com.lizl.mydiary.custom.popup

import android.content.Context
import com.lizl.mydiary.R
import com.lxj.xpopup.core.CenterPopupView
import kotlinx.android.synthetic.main.dialog_loading.view.*

class PopupLoading(context: Context, private val loadingText: String) : CenterPopupView(context)
{
    override fun getImplLayoutId() = R.layout.dialog_loading

    override fun onCreate()
    {
        tv_text.text = loadingText
        loading_view.smoothToShow()
    }
}