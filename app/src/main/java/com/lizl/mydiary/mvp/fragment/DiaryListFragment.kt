package com.lizl.mydiary.mvp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.lizl.mydiary.R
import com.lizl.mydiary.adapter.DiaryListAdapter
import com.lizl.mydiary.bean.BaseDiaryBean
import com.lizl.mydiary.mvp.base.BaseFragment
import com.lizl.mydiary.mvp.contract.DiaryListFragmentContract
import com.lizl.mydiary.mvp.presenter.DiaryListFragmentPresenter
import com.lizl.mydiary.util.AppConstant
import kotlinx.android.synthetic.main.fragment_diray_list.*

class DiaryListFragment : BaseFragment<DiaryListFragmentPresenter>(), DiaryListFragmentContract.View
{

    private val diaryListAdapter: DiaryListAdapter by lazy { DiaryListAdapter() }

    override fun getLayoutResId() = R.layout.fragment_diray_list

    override fun initPresenter() = DiaryListFragmentPresenter(this)

    override fun initView()
    {
        rv_diary_list.layoutManager = LinearLayoutManager(context)
        rv_diary_list.adapter = diaryListAdapter
        val footerView = LayoutInflater.from(context).inflate(R.layout.layout_end_footer, rv_diary_list, false)
        diaryListAdapter.setFooter(footerView)

        fab_add_diary.setOnClickListener {
            turnToFragment(R.id.diaryContentFragment)
        }

        diaryListAdapter.setOnDiaryItemClick {
            val bundle = Bundle()
            bundle.putSerializable(AppConstant.BUNDLE_DATA, it)
            turnToFragment(R.id.diaryContentFragment, bundle)
        }

        diaryListAdapter.clear()
        presenter.loadMoreDiary()
    }

    override fun onMoreDiaries(diaryList: List<BaseDiaryBean>, noMoreData: Boolean)
    {
        diaryListAdapter.addAll(diaryList)
    }
}