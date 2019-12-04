package com.lizl.mydiary.mvp.fragment

import android.text.TextUtils
import com.lizl.mydiary.R
import com.lizl.mydiary.UiApplication
import com.lizl.mydiary.bean.SettingBean
import com.lizl.mydiary.config.ConfigConstant
import com.lizl.mydiary.mvp.presenter.EmptyPresenter
import com.lizl.mydiary.util.BiometricAuthenticationUtil
import com.lizl.mydiary.util.DialogUtil

class SecuritySettingFragment : BaseSettingListFragment<EmptyPresenter>()
{
    override fun getSettingName() = getString(R.string.setting_security)

    override fun getSettingPresenter() = EmptyPresenter()

    override fun initSettingData()
    {
        val fingerprintItem = SettingBean.SettingBooleanBean(getString(R.string.setting_fingerprint), ConfigConstant.IS_FINGERPRINT_LOCK_ON,
                ConfigConstant.DEFAULT_IS_FINGERPRINT_LOCK_ON, true) { result, bean -> }

        val modifyPasswordItem = SettingBean.SettingNormalBean(getString(R.string.setting_modify_password)) {
            DialogUtil.showModifyPasswordDialog(context!!, UiApplication.appConfig.getAppLockPassword()) {
                UiApplication.appConfig.setAppLockPassword(it)
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
                    UiApplication.appConfig.setAppLockPassword(it)
                    UiApplication.appConfig.setAppLockOn(true)
                    settingAdapter.update(bean)
                    var insertPosition = settingAdapter.getPosition(bean)
                    if (BiometricAuthenticationUtil.instance.isFingerprintSupport())
                    {
                        settingAdapter.insert(fingerprintItem, ++insertPosition)
                    }
                    settingAdapter.insert(modifyPasswordItem, ++insertPosition)
                    settingAdapter.insert(lockTimeItem, ++insertPosition)
                }

                if (TextUtils.isEmpty(UiApplication.appConfig.getAppLockPassword()))
                {
                    DialogUtil.showSetPasswordDialog(context!!, onInputFinishListener)
                }
                else
                {
                    DialogUtil.showPasswordConfirmDialog(context!!, UiApplication.appConfig.getAppLockPassword(), onInputFinishListener)
                }
            }
            else
            {
                DialogUtil.showPasswordConfirmDialog(context!!, UiApplication.appConfig.getAppLockPassword()) {
                    UiApplication.appConfig.setAppLockOn(false)
                    settingAdapter.update(bean)
                    settingAdapter.remove(fingerprintItem)
                    settingAdapter.remove(modifyPasswordItem)
                    settingAdapter.remove(lockTimeItem)
                }
            }
        })

        if (UiApplication.appConfig.isAppLockOn())
        {
            if (BiometricAuthenticationUtil.instance.isFingerprintSupport())
            {
                settingList.add(fingerprintItem)
            }
            settingList.add(modifyPasswordItem)
            settingList.add(lockTimeItem)
        }
    }
}