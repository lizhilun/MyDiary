package com.lizl.mydiary.mvp.presenter

import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.event.UIEvent
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

            val searchResult = mutableListOf<DiaryBean>()

            var tagKeyWord = ""
            if (keyword.startsWith("#") && keyword.endsWith("#") && keyword.length > 2)
            {
                tagKeyWord = keyword.substring(1, keyword.length - 1)
            }

            if (isKeywordValid && isMoodValid)
            {
                if (tagKeyWord.isNotBlank())
                {
                    searchResult.addAll(AppDatabase.instance.getDiaryDao().searchDiaryByTag(tagKeyWord).filter { mood == AppConstant.MOOD_ALL || it.mood == mood })
                }
                else
                {
                    searchResult.addAll(AppDatabase.instance.getDiaryDao().searchDiary(keyword).filter { mood == AppConstant.MOOD_ALL || it.mood == mood })
                }
            }
            else if (isKeywordValid && !isMoodValid)
            {
                if (tagKeyWord.isNotBlank())
                {
                    searchResult.addAll(AppDatabase.instance.getDiaryDao().searchDiaryByTag(tagKeyWord))
                }
                else
                {
                    searchResult.addAll(AppDatabase.instance.getDiaryDao().searchDiary(keyword))
                }
            }
            else if (!isKeywordValid && isMoodValid)
            {
                searchResult.addAll(AppDatabase.instance.getDiaryDao().getAllDiary().filter { mood == AppConstant.MOOD_ALL || it.mood == mood })
            }

            GlobalScope.launch(Dispatchers.Main) { view?.showDiaryResult(searchResult) }
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