package com.lizl.mydiary.mvp.fragment

import android.Manifest
import android.content.Context
import com.blankj.utilcode.util.ToastUtils
import com.lizl.mydiary.R
import com.lizl.mydiary.bean.SettingBean
import com.lizl.mydiary.config.AppConfig
import com.lizl.mydiary.config.ConfigConstant
import com.lizl.mydiary.mvp.contract.BackupSettingContract
import com.lizl.mydiary.mvp.presenter.BackupSettingPresenter
import com.lizl.mydiary.util.DialogUtil
import com.lizl.mydiary.util.UiUtil
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions
class BackupSettingFragment : BaseSettingListFragment<BackupSettingPresenter>(), BackupSettingContract.View
{

    private var curWritePermissionsCode = 0
    private val REQUEST_AUTOBACKUP = 1
    private val REQUEST_BACKUPDIARYDATA = 2
    private val REQUEST_RESTOREDIARYDATA = 3

    private lateinit var autoBackupItem: SettingBean.SettingBooleanBean
    private lateinit var autoBackupTimeItem: SettingBean.SettingLongRadioBean

    override fun getSettingName() = getString(R.string.setting_backup_and_restore)

    override fun getSettingPresenter() = BackupSettingPresenter(this)

    override fun initSettingData()
    {
        val timeMap = mapOf(ConfigConstant.APP_AUTO_BACKUP_PERIOD_RIGHT_NOW to getString(R.string.backup_when_modified),
                ConfigConstant.APP_AUTO_BACKUP_PERIOD_1_DAY to "1${getString(R.string.day)}",
                ConfigConstant.APP_AUTO_BACKUP_PERIOD_1_WEEK to "7${getString(R.string.day)}",
                ConfigConstant.APP_AUTO_BACKUP_PERIOD_1_MONTH to "30${getString(R.string.day)}")
        autoBackupTimeItem = SettingBean.SettingLongRadioBean(getString(R.string.setting_auto_backup_interval), ConfigConstant.APP_AUTO_BACKUP_PERIOD,
                ConfigConstant.DEFAULT_APP_AUTO_BACKUP_PERIOD, timeMap) {}

        settingList.add(SettingBean.SettingNormalBean(getString(R.string.setting_backup)) { backupDiaryDataWithPermissionCheck() })

        settingList.add(SettingBean.SettingNormalBean(getString(R.string.setting_restore)) { restoreDiaryDataWithPermissionCheck() })

        settingList.add(SettingBean.SettingDivideBean())

        autoBackupItem = SettingBean.SettingBooleanBean(settingName = getString(R.string.setting_auto_backup), settingKey = ConfigConstant.IS_AUTO_BACKUP_ON,
                defaultValue = ConfigConstant.DEFAULT_IS_AUTO_BACKUP_ON, needSave = false) { result, bean ->
            if (result)
            {
                autoBackupWithPermissionCheck()
            }
            else
            {
                bean.saveValue(false)
                settingAdapter.update(bean)
                settingAdapter.remove(autoBackupTimeItem)
            }
        }
        settingList.add(autoBackupItem)

        if (AppConfig.getBackupConfig().isAutoBackup())
        {
            settingList.add(autoBackupTimeItem)
        }

        settingList.add(SettingBean.SettingDivideBean())

        settingList.add(SettingBean.SettingBooleanBean(settingName = getString(R.string.setting_backup_file_encryption),
                settingKey = ConfigConstant.IS_BACKUP_FILE_ENCRYPTION, defaultValue = ConfigConstant.DEFAULT_IS_BACKUP_FILE_ENCRYPTION,
                needSave = false) { result, bean ->
            if (result)
            {
                if (AppConfig.getSecurityConfig().getAppLockPassword().isEmpty())
                {
                    DialogUtil.showOperationConfirmDialog(activity as Context, getString(R.string.setting_backup_file_encryption),
                            getString(R.string.notify_set_lock_password_before_encryption)) { turnToFragment(R.id.securitySettingFragment) }
                }
                else
                {
                    bean.saveValue(true)
                    settingAdapter.update(bean)
                }
            }
            else
            {
                bean.saveValue(false)
                settingAdapter.update(bean)
            }
        })
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun autoBackup()
    {
        AppConfig.getBackupConfig().setAutoBackup(true)
        settingAdapter.update(autoBackupItem)
        settingAdapter.insert(autoBackupTimeItem, settingAdapter.getPosition(autoBackupItem) + 1)
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun backupDiaryData()
    {
        DialogUtil.showOperationConfirmDialog(context!!, getString(R.string.setting_backup), getString(R.string.notify_backup_data)) {
            presenter.backupData()
        }
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun restoreDiaryData()
    {
        turnToFragment(R.id.backupFileListFragment)
    }

    override fun onStartBackup()
    {
        DialogUtil.showLoadingDialog(context!!, getString(R.string.in_doing, getString(R.string.backup_data)))
    }

    override fun onBackupFinish(result: Boolean)
    {
        DialogUtil.dismissDialog()
        ToastUtils.showShort(getString(R.string.backup_data) + getString(if (result) R.string.success else R.string.failed))
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onPermissionDenied()
    {
        DialogUtil.showOperationConfirmDialog(context!!, getString(R.string.notify_failed_to_get_permission),
                getString(R.string.notify_permission_be_refused)) {
            when (curWritePermissionsCode)
            {
                REQUEST_AUTOBACKUP       -> autoBackupWithPermissionCheck()
                REQUEST_BACKUPDIARYDATA  -> backupDiaryDataWithPermissionCheck()
                REQUEST_RESTOREDIARYDATA -> restoreDiaryDataWithPermissionCheck()
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
}