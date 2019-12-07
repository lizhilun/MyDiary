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

        settingList.add(SettingBean.SettingBooleanBean(getString(R.string.setting_diary_tag), ConfigConstant.LAYOUT_STYLE_SHOW_DIARY_TAG,
                ConfigConstant.DEFAULT_LAYOUT_STYLE_SHOW_DIARY_TAG, true) { _, _ -> })

        settingList.add(SettingBean.SettingDivideBean())

        settingList.add(SettingBean.SettingNormalBean(getString(R.string.setting_font)) { turnToFragment(R.id.fontSettingFragment) })

        settingList.add(SettingBean.SettingDivideBean())

        val imageCountMap = mapOf(3 to "3", 6 to "6", 9 to "9", 18 to "18")
        settingList.add(SettingBean.SettingIntRadioBean(getString(R.string.setting_image_count), ConfigConstant.LAYOUT_STYLE_IMAGE_COUNT,
                ConfigConstant.DEFAULT_LAYOUT_STYLE_IMAGE_COUNT, imageCountMap) {})

        val qualityMap =
            mapOf(ConfigConstant.IMAGE_SAVE_QUALITY_LOW to getString(R.string.low), ConfigConstant.IMAGE_SAVE_QUALITY_MEDIUM to getString(R.string.medium),
                    (ConfigConstant.IMAGE_SAVE_QUALITY_ORIGINAL to getString(R.string.original)))
        settingList.add(SettingBean.SettingIntRadioBean(getString(R.string.setting_image_save_quality), ConfigConstant.IMAGE_SAVE_QUALITY,
                ConfigConstant.DEFAULT_IMAGE_SAVE_QUALITY, qualityMap) {})
    }
}