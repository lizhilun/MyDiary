package com.lizl.mydiary.mvp.contract

import com.lizl.mydiary.bean.BaseDiaryBean
import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.mvp.base.BasePresenter
import com.lizl.mydiary.mvp.base.BaseView

class DiaryListFragmentContract
{
    interface View : BaseView
    {
        fun onDiariesQueryFinish(diaryList: List<BaseDiaryBean>)

        fun onDiaryDeleted(diaryBean: DiaryBean)

        fun showDiarySearchResult(diaryList: List<BaseDiaryBean>)
    }

    interface Presenter : BasePresenter<View>
    {
        fun queryAllDiary()

        fun deleteDiary(diaryBean: DiaryBean)

        fun searchDiary(keyword: String)
    }
}