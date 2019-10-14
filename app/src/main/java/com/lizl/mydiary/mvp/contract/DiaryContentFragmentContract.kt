package com.lizl.mydiary.mvp.contract

import android.content.Intent
import androidx.fragment.app.Fragment
import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.mvp.base.BasePresenter
import com.lizl.mydiary.mvp.base.BaseView

class DiaryContentFragmentContract
{
    interface View : BaseView
    {
        fun onImageSelectedFinish(picList: List<String>)

        fun onDiarySaving()

        fun onDiarySaveSuccess()
    }

    interface Presenter : BasePresenter<View>
    {
        fun selectImage(context: Fragment, maxCount: Int)

        fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

        fun saveDiary(diaryBean: DiaryBean?, content: String, imageList: List<String>)
    }
}