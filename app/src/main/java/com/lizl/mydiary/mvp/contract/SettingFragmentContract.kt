package com.lizl.mydiary.mvp.contract

import com.lizl.mydiary.mvp.base.BasePresenter
import com.lizl.mydiary.mvp.base.BaseView

class SettingFragmentContract
{
    interface View : BaseView
    {
        fun onStartBackup()

        fun onBackupFinish(result: Boolean)
    }

    interface Presenter : BasePresenter<BaseView>
    {
        fun backupData()
    }
}