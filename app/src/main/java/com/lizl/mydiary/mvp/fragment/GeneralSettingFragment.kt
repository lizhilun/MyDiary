package com.lizl.mydiary.mvp.fragment

import android.Manifest
import com.lizl.mydiary.R
import com.lizl.mydiary.bean.SettingBean
import com.lizl.mydiary.config.ConfigConstant
import com.lizl.mydiary.event.EventConstant
import com.lizl.mydiary.event.UIEvent
import com.lizl.mydiary.mvp.presenter.EmptyPresenter
import com.lizl.mydiary.util.DialogUtil
import com.lizl.mydiary.util.SkinUtil
import com.lizl.mydiary.util.UiUtil
import org.greenrobot.eventbus.EventBus
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions
class GeneralSettingFragment : BaseSettingListFragment<EmptyPresenter>()
{

    override fun getSettingName() = getString(R.string.setting)

    override fun getSettingPresenter() = EmptyPresenter()

    override fun initSettingData()
    {
        settingList.add(SettingBean.SettingNormalBean(getString(R.string.setting_security)) { turnToFragment(R.id.securitySettingFragment) })

        settingList.add(SettingBean.SettingNormalBean(getString(R.string.setting_layout_style)) { turnToFragment(R.id.layoutStyleSettingFragment) })

        settingList.add(SettingBean.SettingNormalBean(getString(R.string.setting_backup_and_restore)) { turnToBackupFragmentWithPermissionCheck() })

        settingList.add(SettingBean.SettingDivideBean())

        val nightModeMap = mapOf(ConfigConstant.APP_NIGHT_MODE_ON to getString(R.string.on), ConfigConstant.APP_NIGHT_MODE_OFF to getString(R.string.off),
                ConfigConstant.APP_NIGHT_MODE_FOLLOW_SYSTEM to getString(R.string.follow_system))
        settingList.add(SettingBean.SettingIntRadioBean(settingName = getString(R.string.setting_night_mode), settingKey = ConfigConstant.APP_NIGHT_MODE,
                defaultValue = ConfigConstant.DEFAULT_APP_NIGHT_MODE, radioMap = nightModeMap) { SkinUtil.loadSkin() })

        settingList.add(SettingBean.SettingDivideBean())

        settingList.add(SettingBean.SettingNormalBean(getString(R.string.setting_usage_statistics)) { turnToFragment(R.id.usageStatisticsFragment) })
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun turnToBackupFragment()
    {
        turnToFragment(R.id.backupSettingFragment)
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onPermissionDenied()
    {
        DialogUtil.showOperationConfirmDialog(context!!, getString(R.string.notify_failed_to_get_permission),
                getString(R.string.notify_permission_be_refused)) { turnToBackupFragment() }
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
        onRequestPermissionsResult(requestCode, grantResults)
    }
}