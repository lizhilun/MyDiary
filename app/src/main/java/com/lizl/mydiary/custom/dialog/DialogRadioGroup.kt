package com.lizl.mydiary.custom.dialog

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatRadioButton
import com.lizl.mydiary.R
import kotlinx.android.synthetic.main.dialog_radio_group.*

class DialogRadioGroup(context: Context, private val title: String, private val radioList: List<String>, private val checkedRadio: String,
                       private val onSelectFinishListener: (String) -> Unit) : BaseDialog(context)
{
    override fun getDialogContentViewResId() = R.layout.dialog_radio_group

    override fun initView()
    {
        tv_title.text = title

        radioList.forEach {
            val radioButton = AppCompatRadioButton(context)
            radioButton.setPadding(20, 20, 20, 20)
            radioButton.id = View.generateViewId()
            radioButton.text = it
            rv_radio_group.addView(radioButton, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            if (it == checkedRadio)
            {
                rv_radio_group.check(radioButton.id)
            }
        }

        tv_cancel.setOnClickListener { dismiss() }
        tv_confirm.setOnClickListener {
            val checkedRadio = rv_radio_group.findViewById<AppCompatRadioButton>(rv_radio_group.checkedRadioButtonId)
            onSelectFinishListener.invoke(checkedRadio.text.toString())
            dismiss()
        }
    }

    override fun getDialogWidth() = 0
}