package com.lizl.mydiary.mvp.contract

import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.mvp.base.BasePresenter
import com.lizl.mydiary.mvp.base.BaseView

class DiaryContentFragmentContract
{
    interface View : BaseView
    {}

    interface Presenter : BasePresenter<View>
    {
        fun saveDiary(diaryBean: DiaryBean?, content: String)
    }
}