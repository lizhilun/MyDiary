package com.lizl.mydiary.custom.dialog

import android.content.Context
import androidx.core.widget.addTextChangedListener
import com.lizl.mydiary.R
import com.lizl.mydiary.util.UiUtil
import com.lxj.xpopup.core.CenterPopupView
import kotlinx.android.synthetic.main.popup_input.view.*

/**
 * 用于输入信息的Dialog
 */
class DialogInput(context: Context, private var title: String, private var defaultValue: String?, private var editHint: String,
                  private var inputCompletedCallback: (String) -> Unit) : CenterPopupView(context)
{

    override fun getImplLayoutId() = R.layout.popup_input

    override fun onCreate()
    {
        et_input.hint = editHint
        et_input.setText(defaultValue)

        et_input.addTextChangedListener { tv_confirm.isEnabled = !it.isNullOrEmpty() }

        UiUtil.showInputKeyboard(et_input)

        tv_cancel.setOnClickListener { dismiss() }

        tv_confirm.setOnClickListener {
            inputCompletedCallback.invoke(et_input.text.toString())
            dismiss()
        }
    }

    override fun onDismiss()
    {
        UiUtil.hideInputKeyboard()
    }
}