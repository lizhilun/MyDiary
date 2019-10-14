package com.lizl.mydiary.custom.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import androidx.core.view.isVisible
import com.lizl.mydiary.R
import com.lizl.mydiary.util.UiUtil
import kotlinx.android.synthetic.main.layout_base_dialog.*


abstract class BaseDialog(context: Context, private val title: String?, private val hasBottomButton: Boolean) : Dialog(context, R.style.GlobalDialogStyle)
{
    constructor(context: Context, title: String) : this(context, title, true)

    protected val TAG = javaClass.simpleName

    private var onConfirmButtonClickListener: OnConfirmButtonClickListener? = null
    private var onCancelButtonClickListener: OnCancelButtonClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.layout_base_dialog, null)
        setContentView(view)

        val contentView = layoutInflater.inflate(getDialogContentViewResId(), null)
        fl_content_view.addView(contentView)

        group_bottom_view.isVisible = hasBottomButton
        tv_title.isVisible = !TextUtils.isEmpty(title)
        tv_title.text = title

        tv_confirm.setOnClickListener {
            dismiss()
            onConfirmButtonClickListener?.onConfirmButtonClick()
        }

        tv_cancel.setOnClickListener {
            dismiss()
            onCancelButtonClickListener?.onCancelButtonClick()
        }

        initView()
    }

    override fun onStart()
    {
        super.onStart()

        // 设置Dialog宽度
        val params = window!!.attributes
        val dialogWidth = getDialogWidth()
        params.width = if (dialogWidth == 0) (UiUtil.getScreenWidth() * 0.8).toInt() else dialogWidth
        window!!.attributes = params
    }

    abstract fun getDialogContentViewResId(): Int

    abstract fun initView()

    abstract fun getDialogWidth(): Int

    interface OnConfirmButtonClickListener
    {
        fun onConfirmButtonClick()
    }

    interface OnCancelButtonClickListener
    {
        fun onCancelButtonClick()
    }

    fun setOnConfirmButtonClickListener(onConfirmButtonClickListener: OnConfirmButtonClickListener)
    {
        this.onConfirmButtonClickListener = onConfirmButtonClickListener
    }

    fun setOnCancelButtonClickListener(onCancelButtonClickListener: OnCancelButtonClickListener)
    {
        this.onCancelButtonClickListener = onCancelButtonClickListener
    }
}