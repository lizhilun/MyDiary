package com.lizl.mydiary.mvp.fragment

import android.Manifest
import android.text.TextUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.lizl.mydiary.R
import com.lizl.mydiary.UiApplication
import com.lizl.mydiary.adapter.SettingListAdapter
import com.lizl.mydiary.bean.SettingBean
import com.lizl.mydiary.config.ConfigConstant
import com.lizl.mydiary.mvp.base.BaseFragment
import com.lizl.mydiary.mvp.presenter.EmptyPresenter
import com.lizl.mydiary.util.BackupUtil
import com.lizl.mydiary.util.BiometricAuthenticationUtil
import com.lizl.mydiary.util.DialogUtil
import com.lizl.mydiary.util.UiUtil
import kotlinx.android.synthetic.main.fragment_setting.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions

/**
 * 设置界面
 */
@RuntimePermissions
class SettingFragment : BaseFragment<EmptyPresenter>()
{

    override fun getLayoutResId() = R.layout.fragment_setting

    private lateinit var settingAdapter: SettingListAdapter

    private var curWritePermissionsCode = 0
    private val REQUEST_BACKUPDIARYDATA = 1
    private val REQUEST_RESTOREDIARYDATA = 2

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

        settingAdapter.add(SettingBean.SettingDivideBean())

        settingAdapter.add(SettingBean.SettingNormalBean(getString(R.string.setting_backup)) {
            backupDiaryDataWithPermissionCheck()
        })

        settingAdapter.add(SettingBean.SettingNormalBean(getString(R.string.setting_restore)) {
            restoreDiaryDataWithPermissionCheck()
        })
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun backupDiaryData()
    {
        DialogUtil.showLoadingDialog(context!!, getString(R.string.in_backup_data))
        BackupUtil.backupData {
            ToastUtils.showShort(if (it) R.string.success_to_backup_data else R.string.failed_to_backup_data)
            DialogUtil.dismissDialog()
        }
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun restoreDiaryData()
    {
        turnToFragment(R.id.backupFileListFragment)
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onPermissionDenied()
    {
        DialogUtil.showOperationConfirmDialog(context!!, getString(R.string.notify_failed_to_get_permission),
                getString(R.string.notify_permission_be_refused)) {
            if (curWritePermissionsCode == REQUEST_BACKUPDIARYDATA)
            {
                backupDiaryDataWithPermissionCheck()
            }
            else if (curWritePermissionsCode == REQUEST_RESTOREDIARYDATA)
            {
                restoreDiaryDataWithPermissionCheck()
            }
        }
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onPermissionNeverAskAgain()
    {
        DialogUtil.showOperationConfirmDialog(context!!, getString(R.string.notify_failed_to_get_permission),
                getString(R.string.notify_permission_be_refused_and_never_ask_again)) { UiUtil.goToAppDetailPage() }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // NOTE: delegate the permission handling to generated function
        curWritePermissionsCode = requestCode
        onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun onBackPressed() = false
}