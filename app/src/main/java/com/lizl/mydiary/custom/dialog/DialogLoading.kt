package com.lizl.mydiary.custom.dialog

import android.content.Context
import com.lizl.mydiary.R
import kotlinx.android.synthetic.main.dialog_loading.*

class DialogLoading(context: Context, title: String) : BaseDialog(context, title, false)
{
    override fun getDialogContentViewResId(): Int
    {
        return R.layout.dialog_loading
    }

    override fun initView()
    {
        setCanceledOnTouchOutside(false)
        loading_view.smoothToShow()
    }

    override fun onConfirmButtonClick()
    {

    }

}