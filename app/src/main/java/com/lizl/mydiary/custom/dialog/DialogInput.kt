package com.lizl.mydiary.custom.dialog

import android.content.Context
import androidx.core.widget.addTextChangedListener
import com.lizl.mydiary.R
import com.lizl.mydiary.util.UiUtil
import kotlinx.android.synthetic.main.dialog_input.*

/**
 * 用于输入信息的Dialog
 */
class DialogInput(context: Context, private var title: String, private var defaultValue: String?, private var editHint: String,
                  private var inputCompletedCallback: (String) -> Unit) : BaseDialog(context)
{
    override fun getDialogContentViewResId() = R.layout.dialog_input

    override fun getDialogWidth() = 0

    override fun initView()
    {
        tv_title.text = title
        et_input.hint = editHint
        et_input.setText(defaultValue)

        et_input.addTextChangedListener { tv_confirm.isEnabled = !it.isNullOrEmpty() }

        tv_cancel.setOnClickListener { dismiss() }
        tv_confirm.setOnClickListener {
            inputCompletedCallback.invoke(et_input.text.toString())
            dismiss()
        }

        setOnDismissListener { UiUtil.hideInputKeyboard() }

        UiUtil.showInputKeyboard(et_input)
    }
}