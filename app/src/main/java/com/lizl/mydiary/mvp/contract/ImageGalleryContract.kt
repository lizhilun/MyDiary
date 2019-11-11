package com.lizl.mydiary.mvp.contract

import com.lizl.mydiary.mvp.base.BasePresenter
import com.lizl.mydiary.mvp.base.BaseView

class ImageGalleryContract
{
    interface View : BaseView
    {
        fun showImageList(imageList: List<String>)
    }

    interface Presenter : BasePresenter<View>
    {
        fun getImageList()
    }
}