package com.lizl.mydiary.custom.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.lizl.mydiary.R
import com.lizl.mydiary.util.UiUtil
import kotlinx.android.synthetic.main.dialog_base.*

abstract class BaseDialog(context: Context, private val titleText: String?, private val withBottomBtn: Boolean = false) :
        Dialog(context, R.style.GlobalDialogStyle)
{
    protected val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        val contentView = layoutInflater.inflate(R.layout.dialog_base, null)
        setContentView(contentView)

        with(tv_title) {
            text = titleText
            isVisible = !titleText.isNullOrBlank()
        }

        group_bottom_btn.isVisible = withBottomBtn

        fl_container.addView(getDialogContentView())

        tv_cancel.setOnClickListener { dismiss() }
        tv_confirm.setOnClickListener { if (onConfirmBtnClick()) dismiss() }

        initView()
    }

    protected fun setConfirmBtnEnable(enable: Boolean)
    {
        tv_confirm.isEnabled = enable
    }

    override fun onStart()
    {
        super.onStart()

        // 设置Dialog宽度
        val params = window?.attributes ?: return
        val dialogWidth = getDialogWidth()
        params.width = if (dialogWidth == 0) (UiUtil.getScreenWidth() * 0.8).toInt() else dialogWidth
        window!!.attributes = params
    }

    abstract fun getDialogContentView(): View

    abstract fun initView()

    abstract fun getDialogWidth(): Int

    abstract fun onConfirmBtnClick(): Boolean
}
