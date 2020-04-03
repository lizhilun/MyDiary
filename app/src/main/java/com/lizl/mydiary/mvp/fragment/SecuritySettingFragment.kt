package com.lizl.mydiary.mvp.fragment

import com.lizl.mydiary.R
import com.lizl.mydiary.bean.SettingBean
import com.lizl.mydiary.config.AppConfig
import com.lizl.mydiary.config.ConfigConstant
import com.lizl.mydiary.mvp.presenter.EmptyPresenter
import com.lizl.mydiary.util.BiometricAuthenticationUtil
import com.lizl.mydiary.util.PopupUtil

class SecuritySettingFragment : BaseSettingListFragment<EmptyPresenter>()
{
    override fun getSettingName() = getString(R.string.setting_security)

    override fun getSettingPresenter() = EmptyPresenter()

    override fun initSettingData()
    {
        val fingerprintItem = SettingBean.SettingBooleanBean(getString(R.string.setting_fingerprint), ConfigConstant.IS_FINGERPRINT_LOCK_ON,
                ConfigConstant.DEFAULT_IS_FINGERPRINT_LOCK_ON, true)

        val modifyPasswordItem = SettingBean.SettingNormalBean(getString(R.string.setting_modify_password)) {
            PopupUtil.showModifyPasswordPopup(AppConfig.getSecurityConfig().getAppLockPassword()) {
                AppConfig.getSecurityConfig().setAppLockPassword(it)
            }
        }

        val timeMap = mapOf(ConfigConstant.APP_TIMEOUT_PERIOD_RIGHT_NOW to getString(R.string.lock_when_exit),
                ConfigConstant.APP_TIMEOUT_PERIOD_1_MINUTE to "1${getString(R.string.minute)}",
                ConfigConstant.APP_TIMEOUT_PERIOD_5_MINUTE to "5${getString(R.string.minute)}",
                ConfigConstant.APP_TIMEOUT_PERIOD_10_MINUTE to "10${getString(R.string.minute)}")
        val lockTimeItem = SettingBean.SettingLongRadioBean(getString(R.string.setting_auto_lock_interval), ConfigConstant.APP_TIMEOUT_PERIOD,
                ConfigConstant.DEFAULT_APP_TIMEOUT_PERIOD, timeMap) {}

        settingList.add(SettingBean.SettingBooleanBean(getString(R.string.setting_app_lock), settingKey = ConfigConstant.IS_APP_LOCK_ON,
                defaultValue = ConfigConstant.DEFAULT_IS_APP_LOCK_ON, needSave = false) { result, bean ->
            if (result)
            {
                val onInputFinishListener: (String) -> Unit = {
                    AppConfig.getSecurityConfig().setAppLockPassword(it)
                    AppConfig.getSecurityConfig().setAppLockOn(true)
                    settingAdapter.update(bean)
                    var insertPosition = settingAdapter.getItemPosition(bean)
                    if (BiometricAuthenticationUtil.isFingerprintSupport())
                    {
                        settingAdapter.addData(++insertPosition, fingerprintItem)
                    }
                    settingAdapter.addData(++insertPosition, modifyPasswordItem)
                    settingAdapter.addData(++insertPosition, lockTimeItem)
                }

                if (AppConfig.getSecurityConfig().getAppLockPassword().isBlank())
                {
                    PopupUtil.showSetPasswordPopup(onInputFinishListener)
                }
                else
                {
                    PopupUtil.showPasswordConfirmPopup(AppConfig.getSecurityConfig().getAppLockPassword(), onInputFinishListener)
                }
            }
            else
            {
                PopupUtil.showPasswordConfirmPopup(AppConfig.getSecurityConfig().getAppLockPassword()) {
                    AppConfig.getSecurityConfig().setAppLockOn(false)
                    settingAdapter.update(bean)
                    settingAdapter.remove(fingerprintItem)
                    settingAdapter.remove(modifyPasswordItem)
                    settingAdapter.remove(lockTimeItem)
                }
            }
        })

        if (AppConfig.getSecurityConfig().isAppLockOn())
        {
            if (BiometricAuthenticationUtil.isFingerprintSupport())
            {
                settingList.add(fingerprintItem)
            }
            settingList.add(modifyPasswordItem)
            settingList.add(lockTimeItem)
        }
    }
}