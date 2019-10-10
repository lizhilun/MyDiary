package com.lizl.mydiary.mvp.fragment

import com.lizl.mydiary.R
import com.lizl.mydiary.mvp.base.BaseFragment
import com.lizl.mydiary.mvp.contract.DiaryListFragmentContract
import com.lizl.mydiary.mvp.presenter.DiaryListFragmentPresenter

class DiaryListFragment : BaseFragment<DiaryListFragmentPresenter>(), DiaryListFragmentContract.View
{
    override fun getLayoutResId() = R.layout.fragment_diray_list

    override fun initPresenter() = DiaryListFragmentPresenter(this)

    override fun initView()
    {

    }
}