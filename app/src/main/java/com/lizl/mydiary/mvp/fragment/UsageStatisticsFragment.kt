package com.lizl.mydiary.mvp.fragment

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.lizl.mydiary.R
import com.lizl.mydiary.adapter.CountStatisticsListAdapter
import com.lizl.mydiary.bean.CountStatisticsBean
import com.lizl.mydiary.bean.OperationItem
import com.lizl.mydiary.mvp.activity.DiarySearchActivity
import com.lizl.mydiary.mvp.base.BaseFragment
import com.lizl.mydiary.mvp.contract.UsageStatisticsContract
import com.lizl.mydiary.mvp.presenter.UsageStatisticsPresenter
import com.lizl.mydiary.util.ActivityUtil
import com.lizl.mydiary.util.DialogUtil
import kotlinx.android.synthetic.main.fragment_usage_statistics.*

class UsageStatisticsFragment : BaseFragment<UsageStatisticsPresenter>(), UsageStatisticsContract.View
{

    private lateinit var countStatisticsListAdapter: CountStatisticsListAdapter
    private lateinit var wordStatisticsListAdapter: CountStatisticsListAdapter

    override fun getLayoutResId() = R.layout.fragment_usage_statistics

    override fun initPresenter() = UsageStatisticsPresenter(this)

    override fun initView()
    {
        countStatisticsListAdapter = CountStatisticsListAdapter()
        rv_mood_statistics.layoutManager = LinearLayoutManager(activity)
        rv_mood_statistics.adapter = countStatisticsListAdapter

        wordStatisticsListAdapter = CountStatisticsListAdapter()
        rv_hot_word_statistics.layoutManager = LinearLayoutManager(activity)
        rv_hot_word_statistics.adapter = wordStatisticsListAdapter

        tv_image_count.setOnClickListener { turnToFragment(R.id.imageGalleryFragment) }

        ctb_title.setOnBackBtnClickListener { backToPreFragment() }

        presenter.getUsageStatistics()

        countStatisticsListAdapter.setOnItemClickListener {
            if (it is CountStatisticsBean.MoodStatisticsBean)
            {
                ActivityUtil.turnToActivity(DiarySearchActivity::class.java, it.mood)
            }
        }

        wordStatisticsListAdapter.setOnItemClickListener {
            if (it is CountStatisticsBean.HotWordStatisticsBean)
            {
                ActivityUtil.turnToActivity(DiarySearchActivity::class.java, it.word)
            }
        }

        wordStatisticsListAdapter.setOnItemLongClickListener {
            if (it is CountStatisticsBean.HotWordStatisticsBean)
            {
                val operationList = mutableListOf<OperationItem>()
                operationList.add(OperationItem(getString(R.string.ignore)) {
                    presenter.ignoreHotWord(it.word)
                })
                DialogUtil.showOperationListDialog(activity as Context, operationList)
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

    override fun showHotWordStatistics(hotWordStatisticsList: List<CountStatisticsBean.HotWordStatisticsBean>)
    {
        wordStatisticsListAdapter.setData(hotWordStatisticsList)
    }
}