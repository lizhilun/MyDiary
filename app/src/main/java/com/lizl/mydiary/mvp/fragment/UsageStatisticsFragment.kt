package com.lizl.mydiary.mvp.fragment

import com.lizl.mydiary.R
import com.lizl.mydiary.mvp.base.BaseFragment
import com.lizl.mydiary.mvp.contract.UsageStatisticsContract
import com.lizl.mydiary.mvp.presenter.UsageStatisticsPresenter
import kotlinx.android.synthetic.main.fragment_usage_statistics.*

class UsageStatisticsFragment : BaseFragment<UsageStatisticsPresenter>(), UsageStatisticsContract.View
{
    override fun getLayoutResId() = R.layout.fragment_usage_statistics

    override fun initPresenter() = UsageStatisticsPresenter(this)

    override fun initTitleBar()
    {
        ctb_title.setOnBackBtnClickListener { backToPreFragment() }
    }

    override fun initView()
    {
        presenter.getUsageStatistics()
    }

    override fun showDiaryCount(count: Int)
    {
        tv_diary_count.text = count.toString()
    }

    override fun showWordCount(count: Int)
    {
        tv_word_count.text = count.toString()
    }

    override fun showImageCount(count: Int)
    {
        tv_image_count.text = count.toString()
    }
}