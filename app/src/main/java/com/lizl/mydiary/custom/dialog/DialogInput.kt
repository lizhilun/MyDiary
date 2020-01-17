package com.lizl.mydiary.custom.dialog

import android.content.Context
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.lizl.mydiary.R
import com.lizl.mydiary.util.UiUtil
import kotlinx.android.synthetic.main.dialog_input.*

/**
 * 用于输入信息的Dialog
 */
class DialogInput(context: Context, private var title: String, private var defaultValue: String?, private var editHint: String,
                  private var inputCompletedCallback: (String) -> Unit) : BaseDialog(context, title, true)
{
    override fun getDialogContentView(): View = layoutInflater.inflate(R.layout.dialog_input, null)

    override fun getDialogWidth() = (UiUtil.getScreenWidth() * 0.9).toInt()

    override fun initView()
    {
        et_input.hint = editHint
        et_input.setText(defaultValue)

        et_input.addTextChangedListener { setConfirmBtnEnable(!it.isNullOrEmpty()) }

        setOnDismissListener { UiUtil.hideInputKeyboard() }

        UiUtil.showInputKeyboard(et_input)
    }

    override fun onConfirmBtnClick(): Boolean
    {
        inputCompletedCallback.invoke(et_input.text.toString())
        return true
    }
}