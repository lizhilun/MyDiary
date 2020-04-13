package com.lizl.mydiary.mvp.presenter

import com.blankj.utilcode.util.ImageUtils
import com.lizl.mydiary.bean.CountStatisticsBean
import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.config.AppConfig
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
    override fun getUsageStatistics()
    {
        GlobalScope.launch {

            val diaryList = AppDatabase.instance.getDiaryDao().getAllDiary()

            GlobalScope.launch(Dispatchers.Main) { view?.showDiaryCount(diaryList.count()) }

            val wordCount = diaryList.sumBy { DiaryUtil.sumStringWord(it.content) }
            GlobalScope.launch(Dispatchers.Main) { view?.showWordCount(wordCount) }

            val imageDir = File(FileUtil.getImageFileSavePath())
            val imageCount = imageDir.listFiles()?.count { ImageUtils.isImage(it) } ?: 0
            GlobalScope.launch(Dispatchers.Main) { view?.showImageCount(imageCount) }

            val moodStatisticsResult = getMoodStatistics(diaryList)
            GlobalScope.launch(Dispatchers.Main) { view?.showMoodStatistics(moodStatisticsResult) }

            if (AppConfig.getLayoutStyleConfig().isDiaryTagEnable())
            {
                val tagStatisticsResult = getTagStatistics(diaryList)
                GlobalScope.launch(Dispatchers.Main) { view?.showTagStatistics(tagStatisticsResult) }
            }
        }
    }

    private fun getMoodStatistics(diaryList: List<DiaryBean>): List<CountStatisticsBean.MoodStatisticsBean>
    {
        return getStatistics(diaryList, { it.mood.toString() }, { CountStatisticsBean.MoodStatisticsBean(it.mood, 0) })
    }

    private fun getTagStatistics(diaryList: List<DiaryBean>): List<CountStatisticsBean.TagStatisticsBean>
    {
        return getStatistics(diaryList, { it.tag.orEmpty() }, { CountStatisticsBean.TagStatisticsBean(it.tag.orEmpty(), 0) })
    }

    private fun <T : CountStatisticsBean.BaseCountStatisticsBean> getStatistics(diaryList: List<DiaryBean>, unique: (diaryBean: DiaryBean) -> String,
                                                                                structure: (diaryBean: DiaryBean) -> T): List<T>
    {
        val statisticsList = mutableListOf<T>()
        val statisticsMap = HashMap<String, T>()
        diaryList.forEach {
            val uniqueSignStr = unique.invoke(it)
            if (uniqueSignStr.isEmpty()) return@forEach
            var bean = statisticsMap[uniqueSignStr]
            if (bean == null)
            {
                bean = structure.invoke(it)
                statisticsMap[uniqueSignStr] = bean
                statisticsList.add(bean)
            }
            bean.count += 1
        }
        statisticsList.sortByDescending { it.count }

        return statisticsList
    }

    override fun onDestroy()
    {
        view = null
    }
}