package com.lizl.mydiary.mvp.presenter

import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.event.EventConstant
import com.lizl.mydiary.event.UIEvent
import com.lizl.mydiary.mvp.contract.DiaryListContract
import com.lizl.mydiary.util.AppDatabase
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
                }
            }
        }
    }

    override fun onDestroy()
    {
        view = null
    }
}