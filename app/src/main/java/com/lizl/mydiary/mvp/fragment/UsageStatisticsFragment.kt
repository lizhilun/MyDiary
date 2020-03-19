package com.lizl.mydiary.mvp.fragment

import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.lizl.mydiary.R
import com.lizl.mydiary.adapter.CountStatisticsListAdapter
import com.lizl.mydiary.bean.CountStatisticsBean
import com.lizl.mydiary.mvp.activity.DiarySearchActivity
import com.lizl.mydiary.mvp.base.BaseFragment
import com.lizl.mydiary.mvp.contract.UsageStatisticsContract
import com.lizl.mydiary.mvp.presenter.UsageStatisticsPresenter
import com.lizl.mydiary.util.ActivityUtil
import kotlinx.android.synthetic.main.fragment_usage_statistics.*

class UsageStatisticsFragment : BaseFragment<UsageStatisticsPresenter>(), UsageStatisticsContract.View
{

    private lateinit var countStatisticsListAdapter: CountStatisticsListAdapter
    private lateinit var timeStatisticsListAdapter: CountStatisticsListAdapter
    private lateinit var tagStatisticsListAdapter: CountStatisticsListAdapter

    override fun getLayoutResId() = R.layout.fragment_usage_statistics

    override fun initPresenter() = UsageStatisticsPresenter(this)

    override fun initView()
    {
        countStatisticsListAdapter = CountStatisticsListAdapter()
        rv_mood_statistics.layoutManager = LinearLayoutManager(activity)
        rv_mood_statistics.adapter = countStatisticsListAdapter

        timeStatisticsListAdapter = CountStatisticsListAdapter()
        rv_time_statistics.layoutManager = LinearLayoutManager(activity)
        rv_time_statistics.adapter = timeStatisticsListAdapter

        tagStatisticsListAdapter = CountStatisticsListAdapter()
        rv_tag_statistics.layoutManager = LinearLayoutManager(activity)
        rv_tag_statistics.adapter = tagStatisticsListAdapter

        tv_image_count.setOnClickListener { turnToFragment(R.id.imageGalleryFragment) }

        ctb_title.setOnBackBtnClickListener { backToPreFragment() }

        presenter.getUsageStatistics()

        countStatisticsListAdapter.setOnItemClickListener {
            if (it is CountStatisticsBean.MoodStatisticsBean)
            {
                ActivityUtil.turnToActivity(DiarySearchActivity::class.java, it.mood)
            }
        }

        tagStatisticsListAdapter.setOnItemClickListener {
            if (it is CountStatisticsBean.TagStatisticsBean)
            {
                ActivityUtil.turnToActivity(DiarySearchActivity::class.java, getString(R.string.diary_tag, it.tag))
            }
        }
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

    override fun showMoodStatistics(moodStatisticsList: List<CountStatisticsBean.MoodStatisticsBean>)
    {
        countStatisticsListAdapter.setData(moodStatisticsList)
    }

    override fun showTimeStatistics(timeStatisticsList: List<CountStatisticsBean.TimeStatisticsBean>)
    {
        timeStatisticsListAdapter.setData(timeStatisticsList)
    }

    override fun showTagStatistics(tagStatisticsList: List<CountStatisticsBean.TagStatisticsBean>)
    {
        rv_tag_statistics.isVisible = true
        tagStatisticsListAdapter.setData(tagStatisticsList)
    }
}