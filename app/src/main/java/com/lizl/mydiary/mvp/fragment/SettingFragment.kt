package com.lizl.mydiary.mvp.fragment

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.lizl.mydiary.R
import com.lizl.mydiary.adapter.SettingListAdapter
import com.lizl.mydiary.bean.SettingBean
import com.lizl.mydiary.config.AppConfig
import com.lizl.mydiary.config.ConfigConstant
import com.lizl.mydiary.mvp.base.BaseFragment
import com.lizl.mydiary.mvp.presenter.EmptyPresenter
import kotlinx.android.synthetic.main.fragment_setting.*

/**
 * 设置界面
 */
class SettingFragment : BaseFragment<EmptyPresenter>()
{

    override fun getLayoutResId() = R.layout.fragment_setting

    override fun initTitleBar()
    {
        ctb_title.setOnBackBtnClickListener { backToPreFragment() }
    }

    override fun initPresenter() = EmptyPresenter()

    override fun initView()
    {
        val settingAdapter = SettingListAdapter(getSettingData())
        rv_setting_list.layoutManager = LinearLayoutManager(activity as Context)
        rv_setting_list.adapter = settingAdapter
    }

    private fun getSettingData(): List<SettingBean.SettingBaseBean>
    {
        val settingList = mutableListOf<SettingBean.SettingBaseBean>()

        settingList.add(SettingBean.SettingDivideBean())

        settingList.add(SettingBean.SettingBooleanBean(getString(R.string.setting_app_lock), settingKey = ConfigConstant.IS_APP_LOCK_ON,
                defaultValue = ConfigConstant.DEFAULT_IS_APP_LOCK_ON, needSave = false) {

        })

        if (AppConfig.instance.isAppLockOn())
        {
            settingList.add(SettingBean.SettingBooleanBean(getString(R.string.setting_fingerprint), ConfigConstant.IS_FINGERPRINT_LOCK_ON,
                    ConfigConstant.DEFAULT_IS_FINGERPRINT_LOCK_ON, false) {})
        }

        return settingList
    }

    override fun onBackPressed() = false
}