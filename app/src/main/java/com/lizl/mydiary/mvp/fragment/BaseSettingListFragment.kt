package com.lizl.mydiary.mvp.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.lizl.mydiary.R
import com.lizl.mydiary.adapter.SettingListAdapter
import com.lizl.mydiary.bean.SettingBean
import com.lizl.mydiary.mvp.base.BaseFragment
import com.lizl.mydiary.mvp.base.BasePresenter
import kotlinx.android.synthetic.main.fragment_setting_list.*

abstract class BaseSettingListFragment<T : BasePresenter<*>> : BaseFragment<T>()
{
    protected lateinit var settingAdapter: SettingListAdapter
    protected val settingList = mutableListOf<SettingBean.SettingBaseBean>()

    override fun getLayoutResId() = R.layout.fragment_setting_list

    override fun initPresenter() = getSettingPresenter()

    override fun initView()
    {
        settingAdapter = SettingListAdapter()
        rv_setting_list.layoutManager = LinearLayoutManager(context!!)
        rv_setting_list.adapter = settingAdapter

        ctb_title.setTitleText(getSettingName())
        ctb_title.setOnBackBtnClickListener { activity?.onBackPressed() }

        settingList.clear()
        settingList.add(SettingBean.SettingDivideBean())
        initSettingData()
        settingAdapter.addAll(settingList)
    }

    abstract fun getSettingName(): String

    abstract fun getSettingPresenter(): T

    abstract fun initSettingData()

}