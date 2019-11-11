package com.lizl.mydiary.mvp.presenter

import com.blankj.utilcode.util.ImageUtils
import com.lizl.mydiary.event.UIEvent
import com.lizl.mydiary.mvp.contract.UsageStatisticsContract
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