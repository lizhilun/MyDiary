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

        val nightModeMap = mapOf(ConfigConstant.APP_NIGHT_MODE_ON to getString(R.string.on), ConfigConstant.APP_NIGHT_MODE_OFF to getString(R.string.off),
                ConfigConstant.APP_NIGHT_MODE_FOLLOW_SYSTEM to getString(R.string.follow_system))
        settingList.add(SettingBean.SettingIntRadioBean(settingName = getString(R.string.setting_night_mode), settingKey = ConfigConstant.APP_NIGHT_MODE,
                defaultValue = ConfigConstant.DEFAULT_APP_NIGHT_MODE, radioMap = nightModeMap) {
            EventBus.getDefault().post(UIEvent(EventConstant.UI_EVENT_NIGHT_MODE_CHANGE))
        })

        settingList.add(SettingBean.SettingDivideBean())

        settingList.add(SettingBean.SettingNormalBean(getString(R.string.setting_usage_statistics)) { turnToFragment(R.id.usageStatisticsFragment) })
    }
}