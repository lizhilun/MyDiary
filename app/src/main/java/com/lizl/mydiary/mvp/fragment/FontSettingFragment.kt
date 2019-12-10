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

        ctb_title.setOnBackBtnClickListener { activity?.onBackPressed() }

        sb_font_size.setOnSeekChangeListener {
            DiaryUtil.setDiaryFontSizeLevel(it)
            updateFontSize(DiaryUtil.getDiaryFontSize())
        }

        sb_font_line_space.setOnSeekChangeListener {
            DiaryUtil.setDiaryFontLineSpaceLevel(it)
            updateFontLineSpace(DiaryUtil.getDiaryLineSpace())
        }
    }

    private fun updateFontSize(fontSize: Float)
    {
        tv_sample.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
        tv_sample_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
    }

    private fun updateFontLineSpace(fontLineSpace: Float)
    {
        tv_sample.setLineSpacing(0F, fontLineSpace)
    }

    private fun IndicatorSeekBar.setOnSeekChangeListener(onSeekChangeListener: (progress: Int) -> Unit)
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
}