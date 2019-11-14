package com.lizl.mydiary.mvp.presenter

import com.lizl.mydiary.event.UIEvent
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
            BackupUtil.manualBackupData {
                GlobalScope.launch(Dispatchers.Main) {
                    view?.onBackupFinish(it)
                }
            }
        }
    }

    override fun handleUIEvent(uiEvent: UIEvent)
    {

    }

    override fun onDestroy()
    {
        view = null
    }
}