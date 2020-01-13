package com.lizl.mydiary.custom.dialog

import android.content.Context
import com.lizl.mydiary.R
import kotlinx.android.synthetic.main.dialog_operation_confirm.*

/**
 * 用于操作确认的Dialog
 */
class DialogOperationConfirm(context: Context, private val title: String, private val notify: String, private val onConfirmBtnClickListener: () -> Unit) : BaseDialog(context)
{

    override fun getDialogContentViewResId() = R.layout.dialog_operation_confirm

    override fun getDialogWidth() = 0

    override fun initView()
    {
        tv_title.text = title
        tv_notify.text = notify

        tv_cancel.setOnClickListener { dismiss() }
        tv_confirm.setOnClickListener {
            dismiss()
            onConfirmBtnClickListener.invoke()
        }
    }
}