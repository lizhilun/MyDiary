package com.lizl.mydiary.mvp.fragment

import android.text.TextUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.lizl.mydiary.R
import com.lizl.mydiary.UiApplication
import com.lizl.mydiary.adapter.SettingListAdapter
import com.lizl.mydiary.bean.SettingBean
import com.lizl.mydiary.config.ConfigConstant
import com.lizl.mydiary.mvp.base.BaseFragment
import com.lizl.mydiary.mvp.presenter.EmptyPresenter
import com.lizl.mydiary.util.BiometricAuthenticationUtil
import com.lizl.mydiary.util.DialogUtil
import kotlinx.android.synthetic.main.fragment_setting.*

/**
 * 设置界面
 */
class SettingFragment : BaseFragment<EmptyPresenter>()
{

    override fun getLayoutResId() = R.layout.fragment_setting

    private lateinit var settingAdapter: SettingListAdapter

    override fun initTitleBar()
    {
        ctb_title.setOnBackBtnClickListener { backToPreFragment() }
    }

    override fun initPresenter() = EmptyPresenter()

    override fun initView()
    {
        settingAdapter = SettingListAdapter()
        rv_setting_list.layoutManager = LinearLayoutManager(context!!)
        rv_setting_list.adapter = settingAdapter

        initSettingData()
    }

    private fun initSettingData()
    {
        settingAdapter.add(SettingBean.SettingDivideBean())

        val fingerprintItem = SettingBean.SettingBooleanBean(getString(R.string.setting_fingerprint), ConfigConstant.IS_FINGERPRINT_LOCK_ON,
                ConfigConstant.DEFAULT_IS_FINGERPRINT_LOCK_ON, true) { result, bean ->
        }

        val modifyPasswordItem = SettingBean.SettingNormalBean(getString(R.string.setting_modify_password)) {
            DialogUtil.showModifyPasswordDialog(context!!, UiApplication.appConfig.getAppLockPassword()) {
                UiApplication.appConfig.setAppLockPassword(it)
            }
        }

        settingAdapter.add(SettingBean.SettingBooleanBean(getString(R.string.setting_app_lock), settingKey = ConfigConstant.IS_APP_LOCK_ON,
                defaultValue = ConfigConstant.DEFAULT_IS_APP_LOCK_ON, needSave = false) { result, bean ->
            if (result)
            {
                if (TextUtils.isEmpty(UiApplication.appConfig.getAppLockPassword()))
                {
                    DialogUtil.showSetPasswordDialog(context!!) {
                        UiApplication.appConfig.setAppLockPassword(it)
                        UiApplication.appConfig.setAppLockOn(true)
                        settingAdapter.update(bean)
                        if (BiometricAuthenticationUtil.instance.isFingerprintSupport())
                        {
                            settingAdapter.insert(fingerprintItem, settingAdapter.getPosition(bean) + 1)
                            settingAdapter.insert(modifyPasswordItem, settingAdapter.getPosition(bean) + 2)
                        }
                        else
                        {
                            settingAdapter.insert(modifyPasswordItem, settingAdapter.getPosition(bean) + 1)
                        }
                    }
                }
                else
                {
                    DialogUtil.showPasswordConfirmDialog(context!!, UiApplication.appConfig.getAppLockPassword()) {
                        UiApplication.appConfig.setAppLockOn(true)
                        settingAdapter.update(bean)
                        if (BiometricAuthenticationUtil.instance.isFingerprintSupport())
                        {
                            settingAdapter.insert(fingerprintItem, settingAdapter.getPosition(bean) + 1)
                            settingAdapter.insert(modifyPasswordItem, settingAdapter.getPosition(bean) + 2)
                        }
                        else
                        {
                            settingAdapter.insert(modifyPasswordItem, settingAdapter.getPosition(bean) + 1)
                        }
                    }
                }
            }
            else
            {
                DialogUtil.showPasswordConfirmDialog(context!!, UiApplication.appConfig.getAppLockPassword()) {
                    UiApplication.appConfig.setAppLockOn(false)
                    settingAdapter.update(bean)
                    settingAdapter.remove(fingerprintItem)
                    settingAdapter.remove(modifyPasswordItem)
                }
            }
        })

        if (UiApplication.appConfig.isAppLockOn())
        {
            if (BiometricAuthenticationUtil.instance.isFingerprintSupport())
            {
                settingAdapter.add(fingerprintItem)
            }
            settingAdapter.add(modifyPasswordItem)
        }
    }


    override fun onBackPressed() = false
}