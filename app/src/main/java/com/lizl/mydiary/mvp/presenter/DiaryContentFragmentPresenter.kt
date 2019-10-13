package com.lizl.mydiary.mvp.presenter

import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.mvp.contract.DiaryContentFragmentContract
import com.lizl.mydiary.util.AppDatabase

class DiaryContentFragmentPresenter(private val view: DiaryContentFragmentContract.View) : DiaryContentFragmentContract.Presenter
{

    override fun saveDiary(diaryBean: DiaryBean?, content: String)
    {
        if (diaryBean == null)
        {
            val newDiaryBean = DiaryBean()
            newDiaryBean.createTime = System.currentTimeMillis()
            newDiaryBean.content = content
            AppDatabase.instance.getDiaryDao().insert(newDiaryBean)
        }
    }
}