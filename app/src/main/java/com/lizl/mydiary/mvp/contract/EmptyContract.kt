package com.lizl.mydiary.mvp.contract

import com.lizl.mydiary.mvp.base.BasePresenter
import com.lizl.mydiary.mvp.base.BaseView


class EmptyContract
{
    interface View : BaseView

    interface Presenter : BasePresenter<View>
}