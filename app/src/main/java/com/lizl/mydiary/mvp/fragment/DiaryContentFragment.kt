package com.lizl.mydiary.mvp.fragment

import android.content.Context
import android.content.Intent
import com.lizl.mydiary.R
import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.bean.TitleBarBtnBean
import com.lizl.mydiary.mvp.base.BaseFragment
import com.lizl.mydiary.mvp.contract.DiaryContentFragmentContract
import com.lizl.mydiary.mvp.presenter.DiaryContentFragmentPresenter
import com.lizl.mydiary.util.AppConstant
import kotlinx.android.synthetic.main.fragment_diary_content.*


class DiaryContentFragment : BaseFragment<DiaryContentFragmentPresenter>(), DiaryContentFragmentContract.View
{

    override fun getLayoutResId() = R.layout.fragment_diary_content

    override fun initPresenter() = DiaryContentFragmentPresenter(this)

    override fun initView()
    {
        val bundle = arguments
        val diaryBean = bundle?.getSerializable(AppConstant.BUNDLE_DATA) as DiaryBean?

        val titleBtnList = mutableListOf<TitleBarBtnBean.BaseBtnBean>()
        titleBtnList.add(TitleBarBtnBean.ImageBtnBean(R.mipmap.ic_confirm) {
            dcv_diary_content.saveDiary()
            backToPreFragment()
        })
        ctb_title.setBtnList(titleBtnList)
        ctb_title.setOnBackBtnClickListener { backToPreFragment() }

        dcv_diary_content.bindParentFragment(this@DiaryContentFragment)
        dcv_diary_content.bindDiaryBean(diaryBean)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        dcv_diary_content.handleActivityResult(requestCode, resultCode, data)
    }
}