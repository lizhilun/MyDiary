package com.lizl.mydiary.mvp.presenter

import android.util.SparseArray
import com.blankj.utilcode.util.ImageUtils
import com.lizl.mydiary.bean.CountStatisticsBean
import com.lizl.mydiary.bean.DateBean
import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.event.UIEvent
import com.lizl.mydiary.mvp.contract.UsageStatisticsContract
import com.lizl.mydiary.util.AppDatabase
import com.lizl.mydiary.util.DiaryUtil
import com.lizl.mydiary.util.FileUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class UsageStatisticsPresenter(private var view: UsageStatisticsContract.View?) : UsageStatisticsContract.Presenter
{
    private var diaryList = mutableListOf<DiaryBean>()

    override fun getUsageStatistics()
    {
        GlobalScope.launch {

            diaryList = AppDatabase.instance.getDiaryDao().getAllDiary()

            GlobalScope.launch(Dispatchers.Main) { view?.showDiaryCount(diaryList.count()) }

            val wordCount = diaryList.sumBy { DiaryUtil.sumStringWord(it.content) }
            GlobalScope.launch(Dispatchers.Main) { view?.showWordCount(wordCount) }

            val imageDir = File(FileUtil.getImageFileSavePath())
            val imageCount = imageDir.listFiles().count { ImageUtils.isImage(it) }
            GlobalScope.launch(Dispatchers.Main) { view?.showImageCount(imageCount) }

            val moodStatisticsResult = getMoodStatistics()
            GlobalScope.launch(Dispatchers.Main) { view?.showMoodStatistics(moodStatisticsResult) }

            val timeStatisticsResult = getTimeStatistics()
            GlobalScope.launch(Dispatchers.Main) { view?.showTimeStatistics(timeStatisticsResult) }
        }
    }

    private fun getMoodStatistics(): List<CountStatisticsBean.MoodStatisticsBean>
    {
        val moodMap = SparseArray<CountStatisticsBean.MoodStatisticsBean>()
        val moodStatisticsResult = mutableListOf<CountStatisticsBean.MoodStatisticsBean>()
        diaryList.forEach {
            var moodStatisticsBean = moodMap[it.mood]
            if (moodStatisticsBean == null)
            {
                moodStatisticsBean = CountStatisticsBean.MoodStatisticsBean(it.mood, 0)
                moodMap.put(it.mood, moodStatisticsBean)
                moodStatisticsResult.add(moodStatisticsBean)
            }
            moodStatisticsBean.count += 1
        }

        moodStatisticsResult.sortByDescending { it.count }

        return moodStatisticsResult
    }

    private fun getTimeStatistics(): List<CountStatisticsBean.TimeStatisticsBean>
    {
        val timeStatisticsList = mutableListOf<CountStatisticsBean.TimeStatisticsBean>()
        val timeMap = SparseArray<CountStatisticsBean.TimeStatisticsBean>()
        diaryList.forEach {
            val dateBean = DateBean(it.createTime)
            var timeStatisticsBean = timeMap[dateBean.hour]
            if (timeStatisticsBean == null)
            {
                timeStatisticsBean = CountStatisticsBean.TimeStatisticsBean(dateBean.hour, 0)
                timeMap.put(dateBean.hour, timeStatisticsBean)
                timeStatisticsList.add(timeStatisticsBean)
            }
            timeStatisticsBean.count += 1
        }
        timeStatisticsList.sortByDescending { it.count }

        return timeStatisticsList
    }

    override fun handleUIEvent(uiEvent: UIEvent)
    {

    }

    override fun onDestroy()
    {
        view = null
    }
}