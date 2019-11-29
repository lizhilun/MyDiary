package com.lizl.mydiary.mvp.fragment

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.lizl.mydiary.R
import com.lizl.mydiary.adapter.HotWordStatisticsListAdapter
import com.lizl.mydiary.adapter.MoodStatisticsListAdapter
import com.lizl.mydiary.bean.HotWordBean
import com.lizl.mydiary.bean.MoodStatisticsBean
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

    private lateinit var moodStatisticsListAdapter: MoodStatisticsListAdapter
    private lateinit var wordStatisticsListAdapter: HotWordStatisticsListAdapter

    override fun getLayoutResId() = R.layout.fragment_usage_statistics

    override fun initPresenter() = UsageStatisticsPresenter(this)

    override fun initView()
    {
        moodStatisticsListAdapter = MoodStatisticsListAdapter()
        rv_mood_statistics.layoutManager = LinearLayoutManager(activity)
        rv_mood_statistics.adapter = moodStatisticsListAdapter

        wordStatisticsListAdapter = HotWordStatisticsListAdapter()
        rv_hot_word_statistics.layoutManager = LinearLayoutManager(activity)
        rv_hot_word_statistics.adapter = wordStatisticsListAdapter

        tv_image_count.setOnClickListener { turnToFragment(R.id.imageGalleryFragment) }

        ctb_title.setOnBackBtnClickListener { backToPreFragment() }

        presenter.getUsageStatistics()

        moodStatisticsListAdapter.setOnMoodItemClickListener { ActivityUtil.turnToActivity(DiarySearchActivity::class.java, it.mood) }

        wordStatisticsListAdapter.setOnWordItemClickListener { ActivityUtil.turnToActivity(DiarySearchActivity::class.java, it.word) }

        wordStatisticsListAdapter.setOnWordItemLongClickListener {
            val operationList = mutableListOf<OperationItem>()
            operationList.add(OperationItem(getString(R.string.ignore)) {
                presenter.ignoreHotWord(it.word)
            })
            DialogUtil.showOperationListDialog(activity as Context, operationList)
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

    override fun showMoodStatistics(moodStatisticsList: List<MoodStatisticsBean>)
    {
        moodStatisticsListAdapter.setData(moodStatisticsList)
    }

    override fun showHotWordStatistics(hotWordList: List<HotWordBean>)
    {
        wordStatisticsListAdapter.setData(hotWordList)
    }
}