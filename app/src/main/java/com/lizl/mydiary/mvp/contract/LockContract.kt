package com.lizl.mydiary.mvp.contract

import com.lizl.mydiary.mvp.base.BasePresenter
import com.lizl.mydiary.mvp.base.BaseView

class LockContract
{
    interface View : BaseView
    {
        fun onUnlockSuccess()
    }

    interface Presenter : BasePresenter<View>
    {
        fun startFingerprintAuthentication()

        fun checkInputPassword(password: String)
    }
}