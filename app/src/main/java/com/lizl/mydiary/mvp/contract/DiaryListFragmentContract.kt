package com.lizl.mydiary.mvp.contract

import com.lizl.mydiary.mvp.base.BasePresenter
import com.lizl.mydiary.mvp.base.BaseView


class DiaryListFragmentContract
{
    interface View : BaseView
    {

    }

    interface Presenter : BasePresenter<View>
    {

    }
}