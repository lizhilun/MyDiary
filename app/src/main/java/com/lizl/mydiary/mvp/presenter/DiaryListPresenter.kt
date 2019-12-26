package com.lizl.mydiary.mvp.presenter

import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.config.AppConfig
import com.lizl.mydiary.config.ConfigConstant
import com.lizl.mydiary.event.EventConstant
import com.lizl.mydiary.event.UIEvent
import com.lizl.mydiary.mvp.contract.DiaryListContract
import com.lizl.mydiary.util.AppDatabase
import com.lizl.mydiary.util.BackupUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DiaryListPresenter(private var view: DiaryListContract.View?) : DiaryListContract.Presenter
{
    override fun queryAllDiary()
    {
        GlobalScope.launch {

            val diaryList = AppDatabase.instance.getDiaryDao().getAllDiary()

            GlobalScope.launch(Dispatchers.Main) {
                view?.onDiariesQueryFinish(diaryList)
            }
        }
    }

    override fun handleUIEvent(uiEvent: UIEvent)
    {
        when (uiEvent.event)
        {
            EventConstant.UI_EVENT_IMPORT_DIARY_DATA  ->
            {
                queryAllDiary()
            }
            EventConstant.UI_EVENT_DIARY_SAVE_SUCCESS ->
            {
                if (uiEvent.value is DiaryBean)
                {
                    view?.onDiarySaveSuccess(uiEvent.value)
                    if (AppConfig.getBackupConfig().isAutoBackup() && AppConfig.getBackupConfig().getAppAutoBackupInterval()
                        == ConfigConstant.APP_AUTO_BACKUP_PERIOD_RIGHT_NOW)
                    {
                        BackupUtil.autoBackup()
                    }
                }
            }
        }
    }

    override fun onDestroy()
    {
        view = null
    }
}