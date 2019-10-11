package com.lizl.mydiary.mvp.contract

import com.lizl.mydiary.bean.BaseDiaryBean
import com.lizl.mydiary.mvp.base.BasePresenter
import com.lizl.mydiary.mvp.base.BaseView

class DiaryListFragmentContract
{
    interface View : BaseView
    {
        fun onMoreDiaries(diaryList: List<BaseDiaryBean>, noMoreData: Boolean)
    }

    interface Presenter : BasePresenter<View>
    {
        fun loadMoreDiary()
    }
}