package com.lizl.mydiary.mvp.fragment

import android.Manifest
import android.app.Activity
import android.text.TextUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.lizl.mydiary.R
import com.lizl.mydiary.UiApplication
import com.lizl.mydiary.adapter.SettingListAdapter
import com.lizl.mydiary.bean.SettingBean
import com.lizl.mydiary.config.ConfigConstant
import com.lizl.mydiary.mvp.base.BaseFragment
import com.lizl.mydiary.mvp.contract.SettingFragmentContract
import com.lizl.mydiary.mvp.presenter.SettingFragmentPresenter
import com.lizl.mydiary.util.BiometricAuthenticationUtil
import com.lizl.mydiary.util.DialogUtil
import com.lizl.mydiary.util.SkinUtil
import com.lizl.mydiary.util.UiUtil
import kotlinx.android.synthetic.main.fragment_setting.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions
import skin.support.SkinCompatManager

/**
 * 设置界面
 */
@RuntimePermissions
class SettingFragment : BaseFragment<SettingFragmentPresenter>(), SettingFragmentContract.View
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

    override fun initPresenter() = SettingFragmentPresenter(this)

    override fun initView()
    {
        settingAdapter = SettingListAdapter()
        rv_setting_list.layoutManager = LinearLayoutManager(context!!)
        rv_setting_list.adapter = settingAdapter

        initSettingData()
    }

    override fun onStartBackup()
    {
        DialogUtil.showLoadingDialog(context!!, getString(R.string.in_backup_data))
    }

    override fun onBackupFinish(result: Boolean)
    {
        DialogUtil.dismissDialog()
        ToastUtils.showShort(if (result) R.string.success_to_backup_data else R.string.failed_to_backup_data)
    }

    private fun initSettingData()
    {
        val settingList = mutableListOf<SettingBean.SettingBaseBean>()

        settingList.add(SettingBean.SettingDivideBean())

        val fingerprintItem = SettingBean.SettingBooleanBean(getString(R.string.setting_fingerprint), ConfigConstant.IS_FINGERPRINT_LOCK_ON,
                ConfigConstant.DEFAULT_IS_FINGERPRINT_LOCK_ON, true) { result, bean -> }

        val modifyPasswordItem = SettingBean.SettingNormalBean(getString(R.string.setting_modify_password)) {
            DialogUtil.showModifyPasswordDialog(context!!, UiApplication.appConfig.getAppLockPassword()) {
                UiApplication.appConfig.setAppLockPassword(it)
            }
        }

        settingList.add(SettingBean.SettingBooleanBean(getString(R.string.setting_app_lock), settingKey = ConfigConstant.IS_APP_LOCK_ON,
                defaultValue = ConfigConstant.DEFAULT_IS_APP_LOCK_ON, needSave = false) { result, bean ->
            if (result)
            {
                val onInputFinishListener: (String) -> Unit = {
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
        }

        settingList.add(SettingBean.SettingDivideBean())

        settingList.add(SettingBean.SettingNormalBean(getString(R.string.setting_backup)) { backupDiaryDataWithPermissionCheck() })

        settingList.add(SettingBean.SettingNormalBean(getString(R.string.setting_restore)) { restoreDiaryDataWithPermissionCheck() })

        settingList.add(SettingBean.SettingDivideBean())

        val qualityMap =
                mapOf(ConfigConstant.IMAGE_SAVE_QUALITY_LOW to getString(R.string.low), ConfigConstant.IMAGE_SAVE_QUALITY_MEDIUM to getString(R.string.medium),
                        (ConfigConstant.IMAGE_SAVE_QUALITY_ORIGINAL to getString(R.string.original)))
        settingList.add(SettingBean.SettingIntRadioBean(getString(R.string.setting_image_save_quality), ConfigConstant.IMAGE_SAVE_QUALITY,
                ConfigConstant.DEFAULT_IMAGE_SAVE_QUALITY, qualityMap) {})

        settingList.add(SettingBean.SettingDivideBean())

        settingList.add(SettingBean.SettingBooleanBean(settingName = getString(R.string.setting_night_mode), settingKey = ConfigConstant.IS_NIGHT_MODE_ON,
                defaultValue = ConfigConstant.DEFAULT_NIGHT_MODE_ON, needSave = true) { result, bean ->
            if(result) SkinUtil.instance.loadNightSkin(activity!!) else SkinUtil.instance.loadDefaultSkin(activity!!)
        })

        settingAdapter.addAll(settingList)
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

    override fun needRegisterEvent() = false
}