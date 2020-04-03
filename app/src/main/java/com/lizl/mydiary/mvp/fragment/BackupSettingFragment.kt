package com.lizl.mydiary.mvp.fragment

import com.blankj.utilcode.util.ToastUtils
import com.lizl.mydiary.R
import com.lizl.mydiary.bean.SettingBean
import com.lizl.mydiary.config.AppConfig
import com.lizl.mydiary.config.ConfigConstant
import com.lizl.mydiary.mvp.contract.BackupSettingContract
import com.lizl.mydiary.mvp.presenter.BackupSettingPresenter
import com.lizl.mydiary.util.DialogUtil
import com.lizl.mydiary.util.PopupUtil

class BackupSettingFragment : BaseSettingListFragment<BackupSettingPresenter>(), BackupSettingContract.View
{

    override fun getSettingName() = getString(R.string.setting_backup_and_restore)

    override fun getSettingPresenter() = BackupSettingPresenter(this)

    override fun initSettingData()
    {
        val timeMap = mapOf(ConfigConstant.APP_AUTO_BACKUP_PERIOD_RIGHT_NOW to getString(R.string.backup_when_modified),
                ConfigConstant.APP_AUTO_BACKUP_PERIOD_1_DAY to "1${getString(R.string.day)}",
                ConfigConstant.APP_AUTO_BACKUP_PERIOD_1_WEEK to "7${getString(R.string.day)}",
                ConfigConstant.APP_AUTO_BACKUP_PERIOD_1_MONTH to "30${getString(R.string.day)}")
        val autoBackupTimeItem = SettingBean.SettingLongRadioBean(getString(R.string.setting_auto_backup_interval), ConfigConstant.APP_AUTO_BACKUP_PERIOD,
                ConfigConstant.DEFAULT_APP_AUTO_BACKUP_PERIOD, timeMap)

        settingList.add(SettingBean.SettingNormalBean(getString(R.string.setting_backup)) {
            PopupUtil.showOperationConfirmPopup(getString(R.string.setting_backup), getString(R.string.notify_backup_data)) {
                presenter.backupData()
            }
        })

        settingList.add(SettingBean.SettingNormalBean(getString(R.string.setting_restore)) { turnToFragment(R.id.backupFileListFragment) })

        settingList.add(SettingBean.SettingDivideBean())

        settingList.add(SettingBean.SettingBooleanBean(settingName = getString(R.string.setting_auto_backup), settingKey = ConfigConstant.IS_AUTO_BACKUP_ON,
                defaultValue = ConfigConstant.DEFAULT_IS_AUTO_BACKUP_ON, needSave = true) { result, bean ->
            if (result)
            {
                settingAdapter.addData(settingAdapter.getItemPosition(bean) + 1, autoBackupTimeItem)
            }
            else
            {
                settingAdapter.remove(autoBackupTimeItem)
            }
        })

        if (AppConfig.getBackupConfig().isAutoBackup())
        {
            settingList.add(autoBackupTimeItem)
        }

        settingList.add(SettingBean.SettingDivideBean())

        settingList.add(SettingBean.SettingBooleanBean(settingName = getString(R.string.setting_backup_file_encryption),
                settingKey = ConfigConstant.IS_BACKUP_FILE_ENCRYPTION, defaultValue = ConfigConstant.DEFAULT_IS_BACKUP_FILE_ENCRYPTION,
                needSave = false) { result, bean ->
            if (result && AppConfig.getSecurityConfig().getAppLockPassword().isEmpty())
            {
                PopupUtil.showOperationConfirmPopup(getString(R.string.setting_backup_file_encryption),
                        getString(R.string.notify_set_lock_password_before_encryption)) { turnToFragment(R.id.securitySettingFragment) }
                return@SettingBooleanBean
            }
            bean.saveValue(result)
            settingAdapter.update(bean)
        })
    }

    override fun onStartBackup()
    {
        PopupUtil.showLoadingPopup(getString(R.string.in_doing, getString(R.string.backup_data)))
    }

    override fun onBackupFinish(result: Boolean)
    {
        DialogUtil.dismissDialog()
        ToastUtils.showShort(getString(R.string.backup_data) + getString(if (result) R.string.success else R.string.failed))
    }
}