package com.lizl.mydiary.custom.view

import android.content.Context
import android.util.AttributeSet
import skin.support.widget.SkinCompatTextView

class PasswordTextView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : SkinCompatTextView(context, attrs, defStyleAttr)
{
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private var inputText: String = ""

    fun add(word: String)
    {
        inputText += word
        update()
    }

    fun backspace()
    {
        if (inputText.isEmpty())
        {
            return
        }
        inputText = inputText.substring(0, inputText.length - 1)
        update()
    }

    fun clear()
    {
        inputText = ""
        update()
    }

    private fun update()
    {
        var showText = ""
        for (i in 1..inputText.length)
        {
            showText += "@"
        }
        text = showText
    }

    fun getInputText() = inputText
}