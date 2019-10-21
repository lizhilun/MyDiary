package com.lizl.mydiary.mvp.fragment

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.lizl.mydiary.R
import com.lizl.mydiary.adapter.DiaryListAdapter
import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.bean.OperationItem
import com.lizl.mydiary.bean.TitleBarBtnBean
import com.lizl.mydiary.mvp.base.BaseFragment
import com.lizl.mydiary.mvp.contract.DiaryListFragmentContract
import com.lizl.mydiary.mvp.presenter.DiaryListFragmentPresenter
import com.lizl.mydiary.util.AppConstant
import com.lizl.mydiary.util.DialogUtil
import kotlinx.android.synthetic.main.fragment_diray_list.*
import kotlinx.android.synthetic.main.layout_diary_list_herder.*

class DiaryListFragment : BaseFragment<DiaryListFragmentPresenter>(), DiaryListFragmentContract.View
{

    private lateinit var diaryListAdapter: DiaryListAdapter

    override fun getLayoutResId() = R.layout.fragment_diray_list

    override fun initPresenter() = DiaryListFragmentPresenter(this)

    override fun initTitleBar()
    {
        val titleBtnList = mutableListOf<TitleBarBtnBean.BaseBtnBean>()
        titleBtnList.add(TitleBarBtnBean.ImageBtnBean(R.mipmap.ic_setting) { turnToFragment(R.id.settingFragment) })
        titleBtnList.add(TitleBarBtnBean.ImageBtnBean(R.mipmap.ic_search) {
            ctb_title.startSearchMode({ presenter.searchDiary(it) }, { presenter.queryAllDiary() })
        })
        ctb_title.setTitleText(getString(R.string.app_name))
        ctb_title.setBtnList(titleBtnList)
    }

    override fun initView()
    {
        diaryListAdapter = DiaryListAdapter()
        rv_diary_list.layoutManager = LinearLayoutManager(context)
        rv_diary_list.adapter = diaryListAdapter

        fab_add_diary.setOnClickListener { turnToDiaryContentFragment(null) }

        diaryListAdapter.setOnDiaryItemClickListener { turnToDiaryContentFragment(it) }

        diaryListAdapter.setOnDiaryItemLongClickListener { showDiaryOperationListDialog(it) }

        presenter.queryAllDiary()
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

    override fun onDiariesQueryFinish(diaryList: List<DiaryBean>)
    {
        diaryListAdapter.clear()
        diaryListAdapter.addAll(diaryList)
        updateDiaryListHeader()
    }

    override fun onDiaryDeleted(diaryBean: DiaryBean)
    {
        diaryListAdapter.remove(diaryBean)
        updateDiaryListHeader()
    }

    override fun showDiarySearchResult(diaryList: List<DiaryBean>)
    {
        diaryListAdapter.clear()
        diaryListAdapter.addAll(diaryList)
        updateDiaryListHeader()
    }

    private fun updateDiaryListHeader()
    {
        tv_end_footer.isVisible = diaryListAdapter.getData().isNotEmpty()
        layout_diary_header.isVisible = diaryListAdapter.getData().isNotEmpty()
        tv_header_content.text = getString(R.string.diary_total_count, diaryListAdapter.getData().size)
    }

    override fun onBackPressed() : Boolean
    {
        if(ctb_title.inSearchMode)
        {
            ctb_title.stopSearchMode()
            return true
        }
        return false
    }

    override fun needRegisterEvent() = false
}