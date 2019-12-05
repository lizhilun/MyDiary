package com.lizl.mydiary.custom.others

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher


class IndentTextWatcher : TextWatcher
{
    var markStart = -1
    var changeCount = -1
    var textLength = 0

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int)
    {
        this.markStart = -1
        this.changeCount = -1
        textLength = s.toString().length
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
    {
        this.markStart = start
        this.changeCount = count
    }

    override fun afterTextChanged(s: Editable)
    {
        val start = this.markStart
        val count = this.changeCount

        if (s.toString().length > textLength && count == 1 && s[start] == '\n')
        {
            indent(s, start)
        }

        this.markStart = -1
        this.changeCount = -1
    }

    private fun indent(s: Editable, start: Int)
    {
        val value = getIndent(s, start)
        if (!TextUtils.isEmpty(value))
        {
            s.insert(start + 1, value)
        }
    }

    private fun getIndent(s: Editable, start: Int): CharSequence?
    {
        var begin = lastIndexOf(s, '\n', start - 1)
        begin = if (begin < 0) 0 else begin + 1
        var end = begin
        var i = begin
        val length = start + 1
        while (i < length)
        {
            val c = s[i]
            if (!isWhitespace(c))
            {
                end = i
                break
            }
            i++
        }

        return if (end >= begin)
        {
            '\u3000'.toString()
        }
        else null
    }

    private fun isWhitespace(c: Char): Boolean
    {
        return (c == ' ' || c == '\t' || c == '\u3000')
    }

    private fun lastIndexOf(s: Editable, ch: Char, fromIndex: Int): Int
    {
        if (ch.toInt() < Character.MIN_SUPPLEMENTARY_CODE_POINT)
        {
            // handle most cases here (ch is a BMP code point or a
            // negative value (invalid code point))
            var i = Math.min(fromIndex, s.length - 1)
            while (i >= 0)
            {
                if (s[i] == ch)
                {
                    return i
                }
                i--
            }
            return -1
        }
        else
        {
            return lastIndexOfSupplementary(s, ch.toInt(), fromIndex)
        }
    }

    private fun lastIndexOfSupplementary(s: Editable, ch: Int, fromIndex: Int): Int
    {
        if (Character.isValidCodePoint(ch))
        {
            val hi = Character.highSurrogate(ch)
            val lo = Character.lowSurrogate(ch)
            var i = Math.min(fromIndex, s.length - 2)
            while (i >= 0)
            {
                if (s[i] == hi && s[i + 1] == lo)
                {
                    return i
                }
                i--
            }
        }
        return -1
    }
}