package com.lizl.mydiary.mvp.contract

import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.mvp.base.BasePresenter
import com.lizl.mydiary.mvp.base.BaseView

class DiaryListContract
{
    interface View : BaseView
    {
        fun onDiariesQueryFinish(diaryList: List<DiaryBean>)

        fun onDiarySaveSuccess(diaryBean: DiaryBean)
    }

    interface Presenter : BasePresenter<View>
    {
        fun queryAllDiary()
    }
}