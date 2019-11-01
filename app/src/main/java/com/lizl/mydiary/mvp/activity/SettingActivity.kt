package com.lizl.mydiary.mvp.activity

import com.lizl.mydiary.R
import com.lizl.mydiary.mvp.base.BaseActivity
import com.lizl.mydiary.mvp.presenter.EmptyPresenter

class SettingActivity : BaseActivity<EmptyPresenter>()
{
    override fun getLayoutResId() = R.layout.activity_setting

    override fun initPresenter() = EmptyPresenter()

    override fun initView()
    {

    }
}