package com.lizl.mydiary.custom.dialog

import android.content.Context
import android.view.View
import com.lizl.mydiary.R
import kotlinx.android.synthetic.main.dialog_operation_confirm.*

/**
 * 用于操作确认的Dialog
 */
class DialogOperationConfirm(context: Context, private val title: String, private val notify: String, private val onConfirmBtnClickListener: () -> Unit) :
        BaseDialog(context, title, true)
{

    override fun getDialogContentView(): View = layoutInflater.inflate(R.layout.dialog_operation_confirm, null)

    override fun getDialogWidth() = 0

    override fun initView()
    {
        tv_notify.text = notify
    }

    override fun onConfirmBtnClick(): Boolean
    {
        onConfirmBtnClickListener.invoke()
        return true
    }
}