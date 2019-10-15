package com.lizl.mydiary.mvp.presenter

import com.lizl.mydiary.R
import com.lizl.mydiary.UiApplication
import com.lizl.mydiary.bean.BaseDiaryBean
import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.bean.DiaryCategoryBean
import com.lizl.mydiary.mvp.contract.DiaryListFragmentContract
import com.lizl.mydiary.util.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DiaryListFragmentPresenter(private val view: DiaryListFragmentContract.View) : DiaryListFragmentContract.Presenter
{
    override fun loadMoreDiary()
    {
        GlobalScope.launch {
            val diaryList = mutableListOf<BaseDiaryBean>()
            val diaryCount = AppDatabase.instance.getDiaryDao().getDiaryCount()
            if (diaryCount > 0)
            {
                diaryList.add(DiaryCategoryBean(UiApplication.instance.getString(R.string.diary_total_count, diaryCount)))
            }
            diaryList.addAll(AppDatabase.instance.getDiaryDao().getAllDiary())

            GlobalScope.launch(Dispatchers.Main) {
                view.onMoreDiaries(diaryList, true)
            }
        }
    }

    override fun deleteDiary(diaryBean: DiaryBean)
    {
        GlobalScope.launch {
            AppDatabase.instance.getDiaryDao().delete(diaryBean)

            GlobalScope.launch(Dispatchers.Main) {
                view.onDiaryDeleted(diaryBean)
            }
        }
    }
}