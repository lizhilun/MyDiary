package com.lizl.mydiary.mvp.presenter

import com.blankj.utilcode.util.ImageUtils
import com.lizl.mydiary.bean.HotWordBean
import com.lizl.mydiary.bean.MoodStatisticsBean
import com.lizl.mydiary.event.UIEvent
import com.lizl.mydiary.mvp.contract.UsageStatisticsContract
import com.lizl.mydiary.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class UsageStatisticsPresenter(private var view: UsageStatisticsContract.View?) : UsageStatisticsContract.Presenter
{
    override fun getUsageStatistics()
    {
        GlobalScope.launch {

            val diaryList = AppDatabase.instance.getDiaryDao().getAllDiary()

            GlobalScope.launch(Dispatchers.Main) {
                view?.showDiaryCount(diaryList.count())
                view?.showWordCount(diaryList.sumBy { DiaryUtil.sumStringWord(it.content) })
            }

            val imageDir = File(FileUtil.getImageFileSavePath())
            val imageCount = imageDir.listFiles().count { ImageUtils.isImage(it) }

            GlobalScope.launch(Dispatchers.Main) { view?.showImageCount(imageCount) }

            val moodStatisticsList = mutableListOf<MoodStatisticsBean>()
            moodStatisticsList.add(MoodStatisticsBean(AppConstant.MOOD_HAPPY, diaryList.count { it.mood == AppConstant.MOOD_HAPPY }))
            moodStatisticsList.add(MoodStatisticsBean(AppConstant.MOOD_NORMAL, diaryList.count { it.mood == AppConstant.MOOD_NORMAL }))
            moodStatisticsList.add(MoodStatisticsBean(AppConstant.MOOD_UNHAPPY, diaryList.count { it.mood == AppConstant.MOOD_UNHAPPY }))

            GlobalScope.launch(Dispatchers.Main) { view?.showMoodStatistics(moodStatisticsList) }

            val hotWordList = HotWordUtil.getHotWordList()
            Collections.sort(hotWordList, HotWordComparator())

            GlobalScope.launch(Dispatchers.Main) { view?.showHotWordStatistics(hotWordList.subList(0, 5)) }
        }
    }

    override fun handleUIEvent(uiEvent: UIEvent)
    {

    }

    override fun onDestroy()
    {
        view = null
    }

    inner class HotWordComparator : Comparator<HotWordBean>
    {
        override fun compare(word1: HotWordBean, word2: HotWordBean): Int
        {
            return if (word1.freq > word2.freq) -1
            else if (word1.freq == word2.freq) 0
            else 1
        }
    }
}