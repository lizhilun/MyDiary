package com.lizl.mydiary.mvp.fragment

import com.lizl.mydiary.R
import com.lizl.mydiary.bean.SettingBean
import com.lizl.mydiary.config.ConfigConstant
import com.lizl.mydiary.event.EventConstant
import com.lizl.mydiary.event.UIEvent
import com.lizl.mydiary.mvp.presenter.EmptyPresenter
import org.greenrobot.eventbus.EventBus

class GeneralSettingFragment : BaseSettingListFragment<EmptyPresenter>()
{

    override fun getSettingName() = getString(R.string.setting)

    override fun getSettingPresenter() = EmptyPresenter()

    override fun initSettingData()
    {
        settingList.add(SettingBean.SettingNormalBean(getString(R.string.setting_security)) { turnToFragment(R.id.securitySettingFragment) })

        settingList.add(SettingBean.SettingNormalBean(getString(R.string.setting_layout_style)) { turnToFragment(R.id.layoutStyleSettingFragment) })

        settingList.add(SettingBean.SettingNormalBean(getString(R.string.setting_backup_and_restore)) { turnToFragment(R.id.backupSettingFragment) })

        settingList.add(SettingBean.SettingDivideBean())

        val qualityMap =
                mapOf(ConfigConstant.IMAGE_SAVE_QUALITY_LOW to getString(R.string.low), ConfigConstant.IMAGE_SAVE_QUALITY_MEDIUM to getString(R.string.medium),
                        (ConfigConstant.IMAGE_SAVE_QUALITY_ORIGINAL to getString(R.string.original)))
        settingList.add(SettingBean.SettingIntRadioBean(getString(R.string.setting_image_save_quality), ConfigConstant.IMAGE_SAVE_QUALITY,
                ConfigConstant.DEFAULT_IMAGE_SAVE_QUALITY, qualityMap) {})

        settingList.add(SettingBean.SettingDivideBean())

        settingList.add(SettingBean.SettingBooleanBean(settingName = getString(R.string.setting_night_mode), settingKey = ConfigConstant.IS_NIGHT_MODE_ON,
                defaultValue = ConfigConstant.DEFAULT_NIGHT_MODE_ON, needSave = true) { _, _ ->
            EventBus.getDefault().post(UIEvent(EventConstant.UI_EVENT_NIGHT_MODE_CHANGE))
        })

        settingList.add(SettingBean.SettingDivideBean())

        settingList.add(SettingBean.SettingNormalBean(getString(R.string.setting_usage_statistics)) { turnToFragment(R.id.usageStatisticsFragment) })
    }
}