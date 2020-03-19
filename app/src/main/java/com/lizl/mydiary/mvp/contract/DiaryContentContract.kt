package com.lizl.mydiary.mvp.contract

import android.net.Uri
import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.mvp.base.BasePresenter
import com.lizl.mydiary.mvp.base.BaseView

class DiaryContentContract
{
    interface View : BaseView
    {
        fun onImageSelectedFinish(picList: List<String>)

        fun onDiarySaving()

        fun onDiarySaveSuccess()
    }

    interface Presenter : BasePresenter<View>
    {
        fun handleImageSelectSuccess(imageList: List<Uri>)

        fun saveDiary(diaryBean: DiaryBean?, content: String, imageList: List<String>, createTime: Long, diaryMood: Int, diaryTag: String)
    }
}