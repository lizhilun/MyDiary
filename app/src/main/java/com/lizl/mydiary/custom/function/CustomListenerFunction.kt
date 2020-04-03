package com.lizl.mydiary.custom.function

import androidx.viewpager2.widget.ViewPager2
import com.jungly.gridpasswordview.GridPasswordView
import com.warkiz.widget.IndicatorSeekBar
import com.warkiz.widget.OnSeekChangeListener
import com.warkiz.widget.SeekParams

fun ViewPager2.registerOnPageChangeCallback(onPageSelectedListener: (position: Int) -> Unit)
{
    this.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback()
    {
        override fun onPageSelected(position: Int)
        {
            onPageSelectedListener.invoke(position)
        }
    })
}

fun IndicatorSeekBar.setOnSeekChangeListener(onSeekChangeListener: (progress: Int) -> Unit)
{
    this.onSeekChangeListener = object : OnSeekChangeListener
    {
        override fun onSeeking(seekParams: SeekParams)
        {
            onSeekChangeListener.invoke(seekParams.progress)
        }

        override fun onStartTrackingTouch(seekBar: IndicatorSeekBar)
        {

        }

        override fun onStopTrackingTouch(seekBar: IndicatorSeekBar)
        {

        }
    }
}

fun GridPasswordView.setOnPasswordChangedListener(onPasswordChangedListener: (password: String) -> Unit)
{
    this.setOnPasswordChangedListener(object : GridPasswordView.OnPasswordChangedListener
    {
        override fun onInputFinish(psw: String)
        {
            onPasswordChangedListener.invoke(psw)
        }

        override fun onTextChanged(psw: String)
        {

        }
    })
}