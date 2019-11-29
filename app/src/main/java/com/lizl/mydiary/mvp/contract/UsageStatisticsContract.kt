package com.lizl.mydiary.mvp.contract

import com.lizl.mydiary.bean.HotWordBean
import com.lizl.mydiary.bean.MoodStatisticsBean
import com.lizl.mydiary.mvp.base.BasePresenter
import com.lizl.mydiary.mvp.base.BaseView

class UsageStatisticsContract
{
    interface View : BaseView
    {
        fun showDiaryCount(count: Int)

        fun showWordCount(count: Int)

        fun showImageCount(count: Int)

        fun showMoodStatistics(moodStatisticsList: List<MoodStatisticsBean>)

        fun showHotWordStatistics(hotWordList: List<HotWordBean>)
    }

    interface Presenter : BasePresenter<View>
    {
        fun getUsageStatistics()
    }
}