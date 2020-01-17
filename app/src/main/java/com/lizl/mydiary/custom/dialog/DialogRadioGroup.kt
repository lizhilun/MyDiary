package com.lizl.mydiary.custom.dialog

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatRadioButton
import com.lizl.mydiary.R
import com.lizl.mydiary.util.SkinUtil
import kotlinx.android.synthetic.main.dialog_radio_group.*
import skin.support.widget.SkinCompatRadioButton

class DialogRadioGroup(context: Context, title: String, private val radioList: List<String>, private val checkedRadio: String,
                       private val onSelectFinishListener: (String) -> Unit) : BaseDialog(context, title, true)
{

    override fun getDialogContentView(): View = layoutInflater.inflate(R.layout.dialog_radio_group, null)

    override fun initView()
    {
        val padding = context.resources.getDimensionPixelOffset(R.dimen.global_content_padding_content) / 2

        radioList.forEach {
            val radioButton = SkinCompatRadioButton(context)
            radioButton.setPadding(padding, padding, padding, padding)
            radioButton.setTextColor(SkinUtil.getGlobalTextColor())
            radioButton.setButtonDrawable(R.drawable.ic_check_button)
            radioButton.id = View.generateViewId()
            radioButton.text = it
            rv_radio_group.addView(radioButton, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            if (it == checkedRadio)
            {
                rv_radio_group.check(radioButton.id)
            }
        }
    }

    override fun getDialogWidth() = 0

    override fun onConfirmBtnClick(): Boolean
    {
        val checkedRadio = rv_radio_group.findViewById<AppCompatRadioButton>(rv_radio_group.checkedRadioButtonId) ?: return true
        if (this.checkedRadio != checkedRadio.text.toString())
        {
            onSelectFinishListener.invoke(checkedRadio.text.toString())
        }
        return true
    }
}