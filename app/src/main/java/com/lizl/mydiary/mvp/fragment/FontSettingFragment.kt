package com.lizl.mydiary.mvp.fragment

import android.util.TypedValue
import com.lizl.mydiary.R
import com.lizl.mydiary.mvp.base.BaseFragment
import com.lizl.mydiary.mvp.presenter.EmptyPresenter
import com.lizl.mydiary.util.DiaryUtil
import com.warkiz.widget.IndicatorSeekBar
import com.warkiz.widget.OnSeekChangeListener
import com.warkiz.widget.SeekParams
import kotlinx.android.synthetic.main.fragment_setting_font.*

class FontSettingFragment : BaseFragment<EmptyPresenter>()
{
    override fun getLayoutResId() = R.layout.fragment_setting_font

    override fun initPresenter() = EmptyPresenter()

    override fun initView()
    {
        updateFontSize(DiaryUtil.getDiaryFontSize())
        updateFontLineSpace(DiaryUtil.getDiaryLineSpace())

        sb_font_size.setProgress(DiaryUtil.getDiaryFontSizeLevel().toFloat())
        sb_font_line_space.setProgress(DiaryUtil.getDiaryFontLineSpaceLevel().toFloat())

        sb_font_size.onSeekChangeListener = object : OnSeekChangeListener
        {
            override fun onSeeking(seekParams: SeekParams)
            {
                DiaryUtil.setDiaryFontSizeLevel(seekParams.progress)
                updateFontSize(DiaryUtil.getDiaryFontSize())
            }

            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar)
            {

            }

            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar)
            {

            }
        }

        sb_font_line_space.onSeekChangeListener = object : OnSeekChangeListener
        {
            override fun onSeeking(seekParams: SeekParams)
            {
                DiaryUtil.setDiaryFontLineSpaceLevel(seekParams.progress)
                updateFontLineSpace(DiaryUtil.getDiaryLineSpace())
            }

            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar)
            {

            }

            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar)
            {

            }
        }
    }

    private fun updateFontSize(fontSize : Float)
    {
        tv_sample.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
        tv_sample_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
    }

    private fun updateFontLineSpace(fontLineSapce : Float)
    {
        tv_sample.setLineSpacing(0F, fontLineSapce)
    }
}