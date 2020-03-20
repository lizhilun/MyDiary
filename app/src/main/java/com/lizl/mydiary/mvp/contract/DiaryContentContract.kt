package com.lizl.mydiary.mvp.contract

import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.mvp.base.BasePresenter
import com.lizl.mydiary.mvp.base.BaseView

class DiaryContentContract
{
    interface View : BaseView
    {
        fun onDiarySaving()

        fun onDiarySaveSuccess()
    }

    interface Presenter : BasePresenter<View>
    {
        fun saveDiary(diaryBean: DiaryBean?, content: String, imageList: List<String>, createTime: Long, diaryMood: Int, diaryTag: String)
    }
}