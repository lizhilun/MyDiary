package com.lizl.mydiary.mvp.contract

import android.app.Activity
import android.content.Intent
import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.mvp.base.BasePresenter
import com.lizl.mydiary.mvp.base.BaseView

class DiaryContentContract
{
    interface View : BaseView
    {
        fun onImageSelectedFinish(picList: List<String>)

        fun onImageDelete(imagePath: String)

        fun onDiarySaving()

        fun onDiarySaveSuccess()
    }

    interface Presenter : BasePresenter<View>
    {
        fun selectImage(context: Activity, maxCount: Int)

        fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

        fun saveDiary(diaryBean: DiaryBean?, content: String, imageList: List<String>, createTime: Long, diaryMood : Int, diaryTag : String)
    }
}