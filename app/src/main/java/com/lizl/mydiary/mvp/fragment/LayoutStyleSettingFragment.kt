package com.lizl.mydiary.mvp.fragment

import com.lizl.mydiary.R
import com.lizl.mydiary.bean.SettingBean
import com.lizl.mydiary.config.ConfigConstant
import com.lizl.mydiary.mvp.presenter.EmptyPresenter

class LayoutStyleSettingFragment : BaseSettingListFragment<EmptyPresenter>()
{
    override fun getSettingName() = getString(R.string.setting_layout_style)

    override fun getSettingPresenter() = EmptyPresenter()

    override fun initSettingData()
    {
        settingList.add(SettingBean.SettingBooleanBean(getString(R.string.setting_paragraph_head_indent), ConfigConstant.LAYOUT_STYLE_PARAGRAPH_HEAD_INDENT,
                ConfigConstant.DEFAULT_LAYOUT_STYLE_PARAGRAPH_HEAD_INDENT, true) { _, _ -> })
    }
}