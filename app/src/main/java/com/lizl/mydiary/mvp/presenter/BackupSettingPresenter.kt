package com.lizl.mydiary.mvp.presenter

import com.lizl.mydiary.mvp.contract.BackupSettingContract
import com.lizl.mydiary.util.BackupUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BackupSettingPresenter(private var view: BackupSettingContract.View?) : BackupSettingContract.Presenter
{
    override fun backupData()
    {
        GlobalScope.launch {
            GlobalScope.launch(Dispatchers.Main) {
                view?.onStartBackup()
            }
            BackupUtil.manualBackupData {
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