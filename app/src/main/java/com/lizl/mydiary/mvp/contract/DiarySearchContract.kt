package com.lizl.mydiary.mvp.contract

import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.mvp.base.BasePresenter
import com.lizl.mydiary.mvp.base.BaseView

class DiarySearchContract
{
    interface View : BaseView
    {
        fun showDiaryResult(diaryList: List<DiaryBean>)
    }

    interface Presenter : BasePresenter<View>
    {
        fun searchDiary(keyword: String, mood: Int)
    }
}