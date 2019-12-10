package com.lizl.mydiary.custom.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import com.lizl.mydiary.R
import com.lizl.mydiary.util.UiUtil


abstract class BaseDialog(context: Context) : Dialog(context, R.style.GlobalDialogStyle)
{
    protected val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        val contentView = LayoutInflater.from(context).inflate(getDialogContentViewResId(), null)
        setContentView(contentView)

        initView()
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

    abstract fun getDialogContentViewResId(): Int

    abstract fun initView()

    abstract fun getDialogWidth(): Int
}