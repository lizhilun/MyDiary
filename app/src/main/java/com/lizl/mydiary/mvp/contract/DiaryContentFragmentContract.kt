package com.lizl.mydiary.mvp.contract

import android.content.Intent
import androidx.fragment.app.Fragment
import com.lizl.mydiary.mvp.base.BasePresenter
import com.lizl.mydiary.mvp.base.BaseView

class DiaryContentFragmentContract
{
    interface View : BaseView
    {
        fun onImageSelectedFinish(picList: List<String>)
    }

    interface Presenter : BasePresenter<View>
    {
        fun selectImage(context: Fragment)

        fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    }
}