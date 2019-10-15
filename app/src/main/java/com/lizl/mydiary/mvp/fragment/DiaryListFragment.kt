package com.lizl.mydiary.mvp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.lizl.mydiary.R
import com.lizl.mydiary.adapter.DiaryListAdapter
import com.lizl.mydiary.bean.BaseDiaryBean
import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.bean.OperationItem
import com.lizl.mydiary.bean.TitleBarBtnBean
import com.lizl.mydiary.mvp.base.BaseFragment
import com.lizl.mydiary.mvp.contract.DiaryListFragmentContract
import com.lizl.mydiary.mvp.presenter.DiaryListFragmentPresenter
import com.lizl.mydiary.util.AppConstant
import com.lizl.mydiary.util.DialogUtil
import kotlinx.android.synthetic.main.fragment_diray_list.*

class DiaryListFragment : BaseFragment<DiaryListFragmentPresenter>(), DiaryListFragmentContract.View
{
    private lateinit var diaryListAdapter: DiaryListAdapter

    override fun getLayoutResId() = R.layout.fragment_diray_list

    override fun initPresenter() = DiaryListFragmentPresenter(this)

    override fun initTitleBar()
    {
        val titleBtnList = mutableListOf<TitleBarBtnBean.BaseBtnBean>()
        titleBtnList.add(TitleBarBtnBean.ImageBtnBean(R.mipmap.ic_setting) {})
        titleBtnList.add(TitleBarBtnBean.ImageBtnBean(R.mipmap.ic_search) { ctb_title.startSearchMode {presenter.searchDiary(it)} })
        ctb_title.setTitleText(getString(R.string.app_name))
        ctb_title.setBtnList(titleBtnList)
    }

    override fun initView()
    {
        diaryListAdapter = DiaryListAdapter()
        rv_diary_list.layoutManager = LinearLayoutManager(context)
        rv_diary_list.adapter = diaryListAdapter
        val footerView = LayoutInflater.from(context).inflate(R.layout.layout_end_footer, rv_diary_list, false)
        diaryListAdapter.setFooter(footerView)

        fab_add_diary.setOnClickListener { turnToDiaryContentFragment(null) }

        diaryListAdapter.setOnDiaryItemClickListener { turnToDiaryContentFragment(it) }

        diaryListAdapter.setOnDiaryItemClickLongListener { showDiaryOperationListDialog(it) }

        presenter.loadMoreDiary()
    }

    private fun turnToDiaryContentFragment(diaryBean: DiaryBean?)
    {
        if (diaryBean != null)
        {
            val bundle = Bundle()
            bundle.putSerializable(AppConstant.BUNDLE_DATA_OBJECT, diaryBean)
            turnToFragment(R.id.diaryContentFragment, bundle)
        }
        else
        {
            turnToFragment(R.id.diaryContentFragment)
        }
    }

    private fun showDiaryOperationListDialog(diaryBean: DiaryBean)
    {
        val operationList = mutableListOf<OperationItem>()
        operationList.add(OperationItem(getString(R.string.delete)) { presenter.deleteDiary(diaryBean) })
        DialogUtil.showOperationListDialog(context!!, operationList)
    }

    override fun onMoreDiaries(diaryList: List<BaseDiaryBean>, noMoreData: Boolean)
    {
        diaryListAdapter.addAll(diaryList)
    }

    override fun onDiaryDeleted(diaryBean: DiaryBean)
    {
        diaryListAdapter.remove(diaryBean)
    }

    override fun showDiarySearchResult(diaryList: List<BaseDiaryBean>)
    {
        diaryListAdapter.clear()
        diaryListAdapter.addAll(diaryList)
    }
}