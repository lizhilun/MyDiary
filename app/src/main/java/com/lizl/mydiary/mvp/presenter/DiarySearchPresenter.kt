package com.lizl.mydiary.mvp.presenter

import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.mvp.contract.DiarySearchContract
import com.lizl.mydiary.util.AppConstant
import com.lizl.mydiary.util.AppDatabase
import com.lizl.mydiary.util.DiaryUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DiarySearchPresenter(private var view: DiarySearchContract.View?) : DiarySearchContract.Presenter
{
    override fun searchDiary(keyword: String, mood: Int)
    {
        GlobalScope.launch {

            val isKeywordValid = keyword.isNotBlank()
            val isMoodValid = DiaryUtil.getMoodList(true).contains(mood)
            val tagKeyWord = if (keyword.startsWith("#") && keyword.endsWith("#") && keyword.length > 2)
            {
                keyword.substring(1, keyword.length - 1)
            }
            else ""
            val moodSelection = { diaryBean: DiaryBean -> if (isMoodValid) (mood == AppConstant.MOOD_ALL || diaryBean.mood == mood) else true }

            val searchResult = when
            {
                tagKeyWord.isNotBlank() -> AppDatabase.instance.getDiaryDao().searchDiaryByTag(tagKeyWord).filter(moodSelection)
                isKeywordValid          -> AppDatabase.instance.getDiaryDao().searchDiary(keyword).filter(moodSelection)
                isMoodValid             -> AppDatabase.instance.getDiaryDao().getAllDiary().filter(moodSelection)
                else                    -> emptyList()
            }

            GlobalScope.launch(Dispatchers.Main) { view?.showDiaryResult(searchResult) }
        }
    }

    override fun onDestroy()
    {
        view = null
    }
}