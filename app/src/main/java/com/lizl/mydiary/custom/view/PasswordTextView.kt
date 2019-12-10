package com.lizl.mydiary.custom.view

import android.content.Context
import android.util.AttributeSet
import skin.support.widget.SkinCompatTextView

class PasswordTextView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : SkinCompatTextView(context, attrs, defStyleAttr)
{
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private var inputText = StringBuilder()

    fun add(word: String)
    {
        inputText.append(word)
        update()
    }

    fun backspace()
    {
        if (inputText.isEmpty()) return
        inputText.deleteCharAt(inputText.length - 1)
        update()
    }

    fun clear()
    {
        inputText.clear()
        update()
    }

    private fun update()
    {
        val showText = StringBuilder()
        for (i in 1..inputText.length)
        {
            showText.append("@")
        }
        text = showText.toString()
    }

    fun getInputText() = inputText.toString()
}