package com.lizl.mydiary.mvp.presenter

import android.text.TextUtils
import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.event.EventConstant
import com.lizl.mydiary.event.UIEvent
import com.lizl.mydiary.mvp.contract.DiaryListContract
import com.lizl.mydiary.util.AppDatabase
import com.lizl.mydiary.util.FileUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DiaryListPresenter(private var view: DiaryListContract.View?) : DiaryListContract.Presenter
{
    private val diaryList = mutableListOf<DiaryBean>()

    override fun queryAllDiary()
    {
        GlobalScope.launch {

            diaryList.clear()
            diaryList.addAll(AppDatabase.instance.getDiaryDao().getAllDiary())

            GlobalScope.launch(Dispatchers.Main) {
                view?.onDiariesQueryFinish(diaryList)
            }
        }
    }

    override fun deleteDiary(diaryBean: DiaryBean)
    {
        GlobalScope.launch {
            diaryBean.imageList?.forEach { FileUtil.deleteFile(it) }
            AppDatabase.instance.getDiaryDao().delete(diaryBean)

            GlobalScope.launch(Dispatchers.Main) {
                view?.onDiaryDeleted(diaryBean)
            }
        }
    }

    override fun searchDiary(keyword: String)
    {
        if (TextUtils.isEmpty(keyword))
        {
            queryAllDiary()
            return
        }
        GlobalScope.launch {
            val diaryList = mutableListOf<DiaryBean>()
            diaryList.addAll(AppDatabase.instance.getDiaryDao().searchDiary(keyword))
            GlobalScope.launch(Dispatchers.Main) {
                view?.showDiarySearchResult(diaryList)
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
                    val findDiaryBean = diaryList.find { it.uid == uiEvent.value.uid }
                    if (findDiaryBean != null)
                    {
                        diaryList.remove(findDiaryBean)
                        view?.onDiaryDeleted(findDiaryBean)
                    }
                    diaryList.add(uiEvent.value)
                    view?.onDiaryInsert(uiEvent.value)
                }
            }
        }
    }

    override fun onDestroy()
    {
        view = null
    }
}