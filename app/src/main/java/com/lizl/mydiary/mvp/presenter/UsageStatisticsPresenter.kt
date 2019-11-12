package com.lizl.mydiary.mvp.presenter

import com.blankj.utilcode.util.ImageUtils
import com.lizl.mydiary.bean.MoodStatisticsBean
import com.lizl.mydiary.event.UIEvent
import com.lizl.mydiary.mvp.contract.UsageStatisticsContract
import com.lizl.mydiary.util.AppConstant
import com.lizl.mydiary.util.AppDatabase
import com.lizl.mydiary.util.FileUtil
import com.lizl.mydiary.util.UiUtil
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

            GlobalScope.launch(Dispatchers.Main) {
                view?.showDiaryCount(diaryList.count())
                view?.showWordCount(diaryList.sumBy { UiUtil.sumStringWord(it.content) })
            }

            val imageDir = File(FileUtil.getImageFileSavePath())
            val imageCount = imageDir.listFiles().count { ImageUtils.isImage(it) }

            GlobalScope.launch(Dispatchers.Main) { view?.showImageCount(imageCount) }

            var moodHappy = 0
            var moodNormal = 0
            var moodUnhappy = 0

            diaryList.forEach {
                when (it.mood)
                {
                    AppConstant.MOOD_HAPPY   -> moodHappy++
                    AppConstant.MOOD_NORMAL  -> moodNormal++
                    AppConstant.MOOD_UNHAPPY -> moodUnhappy++
                }
            }

            val moodStatisticsList = mutableListOf<MoodStatisticsBean>()
            moodStatisticsList.add(MoodStatisticsBean(AppConstant.MOOD_HAPPY, moodHappy))
            moodStatisticsList.add(MoodStatisticsBean(AppConstant.MOOD_NORMAL, moodNormal))
            moodStatisticsList.add(MoodStatisticsBean(AppConstant.MOOD_UNHAPPY, moodUnhappy))

            GlobalScope.launch(Dispatchers.Main) { view?.showMoodStatistics(moodStatisticsList) }
        }
    }

    override fun handleUIEvent(uiEvent: UIEvent)
    {

    }

    override fun onDestroy()
    {
        view = null
    }
}