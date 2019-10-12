package com.lizl.mydiary.mvp.fragment

import android.content.Intent
import com.lizl.mydiary.bean.TitleBarBtnBean
import com.lizl.mydiary.mvp.base.BaseFragment
import com.lizl.mydiary.mvp.contract.DiaryContentFragmentContract
import com.lizl.mydiary.mvp.presenter.DiaryContentFragmentPresenter
import kotlinx.android.synthetic.main.fragment_diary_content.*


class DiaryContentFragment : BaseFragment<DiaryContentFragmentPresenter>(), DiaryContentFragmentContract.View
{

    override fun getLayoutResId() = com.lizl.mydiary.R.layout.fragment_diary_content

    override fun initPresenter() = DiaryContentFragmentPresenter(this)

    override fun initView()
    {
        val titleBtnList = mutableListOf<TitleBarBtnBean.BaseBtnBean>()
        titleBtnList.add(TitleBarBtnBean.TextBtnBean("ADD") {
            presenter.selectImage(this@DiaryContentFragment)
        })
        ctb_title.setBtnList(titleBtnList)
        ctb_title.setOnBackBtnClickListener { backToPreFragment() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        presenter.handleActivityResult(requestCode, resultCode, data)
    }

    override fun onImageSelectedFinish(picList: List<String>)
    {
        et_content.measure(0, 0)
        for (picPath in picList) et_content.insertImage(picPath, et_content.measuredWidth)
    }
}