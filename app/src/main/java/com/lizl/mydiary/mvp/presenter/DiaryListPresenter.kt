package com.lizl.mydiary.mvp.presenter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.lizl.mydiary.config.AppConfig
import com.lizl.mydiary.config.ConfigConstant
import com.lizl.mydiary.mvp.contract.DiaryListContract
import com.lizl.mydiary.util.AppDatabase
import com.lizl.mydiary.util.BackupUtil

class DiaryListPresenter(private var view: DiaryListContract.View?) : DiaryListContract.Presenter
{
    override fun queryAllDiary(owner: LifecycleOwner)
    {
        AppDatabase.instance.getDiaryDao().getAllDiaryLiveData().observe(owner, Observer {
            view?.onDiariesQueryFinish(it ?: emptyList())

            if (AppConfig.getBackupConfig().isAutoBackup() && AppConfig.getBackupConfig().getAppAutoBackupInterval()
                == ConfigConstant.APP_AUTO_BACKUP_PERIOD_RIGHT_NOW)
            {
                BackupUtil.autoBackup()
            }
        })
    }

    override fun onDestroy()
    {
        view = null
    }
}