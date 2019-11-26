package com.lizl.mydiary.util

import android.text.TextUtils
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ScreenUtils


/**
 * UI相关工具类
 */
class UiUtil
{
    companion object
    {

        /**
         * 隐藏输入法
         */
        fun hideInputKeyboard(view: View) = KeyboardUtils.hideSoftInput(view)

        /**
         * 隐藏输入法
         */
        fun hideInputKeyboard()
        {
            if (KeyboardUtils.isSoftInputVisible(ActivityUtils.getTopActivity()))
            {
                KeyboardUtils.toggleSoftInput()
            }
        }

        /**
         * 显示输入法
         */
        fun showInputKeyboard(view: View) = KeyboardUtils.showSoftInput(view)

        /**
         * 退回到桌面
         */
        fun backToLauncher() = ActivityUtils.startHomeActivity()

        /**
         * 跳转到APP详情界面（用于获取权限）
         */
        fun goToAppDetailPage() = AppUtils.launchAppDetailsSettings()

        /**
         * 去除TextView默认换行样式，自定义换行
         */
        fun clearTextViewAutoWrap(tv: TextView)
        {
            if (TextUtils.isEmpty(tv.text))
            {
                return
            }
            tv.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener
            {
                override fun onGlobalLayout()
                {
                    tv.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val newText = autoSplitText(tv)
                    tv.text = newText
                }
            })
        }

        /**
         * 自定义TextView换行
         */
        private fun autoSplitText(tv: TextView): String
        {
            val rawText = tv.text.toString() //原始文本
            val tvPaint = tv.paint //paint，包含字体等信息
            val tvWidth = (tv.width - tv.paddingLeft - tv.paddingRight).toFloat() //控件可用宽度

            //将原始文本按行拆分
            val rawTextLines = rawText.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val sb = StringBuilder()
            for (rawTextLine in rawTextLines)
            {
                if (tvPaint.measureText(rawTextLine) <= tvWidth)
                {
                    //如果整行宽度在控件可用宽度之内，就不处理了
                    sb.append(rawTextLine)
                }
                else
                {
                    //如果整行宽度超过控件可用宽度，则按字符测量，在超过可用宽度的前一个字符处手动换行
                    var lineWidth = 0f
                    var cnt = 0
                    while (cnt != rawTextLine.length)
                    {
                        val ch = rawTextLine[cnt]
                        lineWidth += tvPaint.measureText(ch.toString())
                        if (lineWidth <= tvWidth)
                        {
                            sb.append(ch)
                        }
                        else
                        {
                            sb.append("\n")
                            lineWidth = 0f
                            --cnt
                        }
                        ++cnt
                    }
                }
                sb.append("\n")
            }

            //把结尾多余的\n去掉
            if (!rawText.endsWith("\n"))
            {
                sb.deleteCharAt(sb.length - 1)
            }
            return sb.toString()
        }

        /**
         * 获取屏幕宽度
         */
        fun getScreenWidth(): Int = ScreenUtils.getScreenWidth()

        /**
         * 获取屏幕高度
         */
        fun getScreenHeight(): Int = ScreenUtils.getScreenHeight()
    }
}