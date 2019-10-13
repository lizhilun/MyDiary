package com.lizl.mydiary.mvp.presenter

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
            diaryList.add(DiaryCategoryBean("test"))
            diaryList.addAll(AppDatabase.instance.getDiaryDao().getAllDiary())

            GlobalScope.launch(Dispatchers.Main) {
                view.onMoreDiaries(diaryList, true)
            }
        }
    }
}