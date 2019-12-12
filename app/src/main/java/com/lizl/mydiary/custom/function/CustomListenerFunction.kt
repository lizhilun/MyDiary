package com.lizl.mydiary.custom.function

import androidx.viewpager.widget.ViewPager
import com.jungly.gridpasswordview.GridPasswordView
import com.warkiz.widget.IndicatorSeekBar
import com.warkiz.widget.OnSeekChangeListener
import com.warkiz.widget.SeekParams

fun ViewPager.addOnPageChangeListener(onPageSelectedListener: (position: Int) -> Unit)
{
    this.addOnPageChangeListener(object : ViewPager.OnPageChangeListener
    {
        override fun onPageScrollStateChanged(p0: Int)
        {
        }

        override fun onPageScrolled(p0: Int, p1: Float, p2: Int)
        {
        }

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