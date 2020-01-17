package com.lizl.mydiary.custom.dialog

import android.content.Context
import android.view.View
import com.lizl.mydiary.R
import com.lizl.mydiary.util.UiUtil
import kotlinx.android.synthetic.main.dialog_loading.*

class DialogLoading(context: Context, private val loadingText: String) : BaseDialog(context, null)
{

    override fun getDialogContentView(): View = layoutInflater.inflate(R.layout.dialog_loading, null)

    override fun initView()
    {
        tv_text.text = loadingText
        setCanceledOnTouchOutside(false)
        loading_view.smoothToShow()
    }

    override fun getDialogWidth() = (UiUtil.getScreenWidth() * 0.9).toInt()

    override fun onConfirmBtnClick() = true
}