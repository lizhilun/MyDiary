package com.lizl.mydiary.bean

open class TitleBarBtnBean
{
    open class BaseBtnBean

    class ImageBtnBean(val imageRedId: Int, val onBtnClickListener: () -> Unit) : BaseBtnBean()

    class TextBtnBean(val text: String, val onBtnClickListener: () -> Unit) : BaseBtnBean()

    interface OnBtnClickListener
    {
        fun onBtnClick()
    }
}