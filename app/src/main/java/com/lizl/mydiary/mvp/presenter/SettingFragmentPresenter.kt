package com.lizl.mydiary.mvp.presenter

import com.lizl.mydiary.mvp.contract.SettingFragmentContract
import com.lizl.mydiary.util.BackupUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SettingFragmentPresenter(private var view: SettingFragmentContract.View?) : SettingFragmentContract.Presenter
{
    override fun backupData()
    {
        GlobalScope.launch {
            GlobalScope.launch(Dispatchers.Main) {
                view?.onStartBackup()
            }
            BackupUtil.backupData {
                GlobalScope.launch(Dispatchers.Main) {
                    view?.onBackupFinish(it)
                }
            }
        }
    }

    override fun onDestroy()
    {
        view = null
    }
}