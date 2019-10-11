package com.lizl.mydiary.mvp.activity
import com.lizl.mydiary.R
import com.lizl.mydiary.mvp.base.BaseActivity
import com.lizl.mydiary.mvp.contract.EmptyContract
import com.lizl.mydiary.mvp.presenter.EmptyPresenter

class MainActivity : BaseActivity<EmptyPresenter>(), EmptyContract.View
{
    override fun getLayoutResId() = R.layout.activity_main

    override fun initPresenter() = EmptyPresenter()

    override fun initView()
    {

    }
}
