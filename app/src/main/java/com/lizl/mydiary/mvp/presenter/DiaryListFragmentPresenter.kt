package com.lizl.mydiary.mvp.presenter

import android.text.TextUtils
import com.lizl.mydiary.R
import com.lizl.mydiary.UiApplication
import com.lizl.mydiary.bean.BaseDiaryBean
import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.bean.DiaryCategoryBean
import com.lizl.mydiary.mvp.contract.DiaryListFragmentContract
import com.lizl.mydiary.util.AppDatabase
import com.lizl.mydiary.util.FileUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DiaryListFragmentPresenter(private val view: DiaryListFragmentContract.View) : DiaryListFragmentContract.Presenter
{

    override fun queryAllDiary()
    {
        GlobalScope.launch {
            val diaryList = mutableListOf<BaseDiaryBean>()
            val diaryCount = AppDatabase.instance.getDiaryDao().getDiaryCount()
            if (diaryCount > 0)
            {
                diaryList.add(DiaryCategoryBean(UiApplication.instance.getString(R.string.diary_total_count, diaryCount)))
            }
            diaryList.addAll(AppDatabase.instance.getDiaryDao().getAllDiary())

            GlobalScope.launch(Dispatchers.Main) {
                view.onDiariesQueryFinish(diaryList)
            }
        }
    }

    override fun deleteDiary(diaryBean: DiaryBean)
    {
        GlobalScope.launch {
            diaryBean.imageList?.forEach { FileUtil.deleteFile(it) }
            AppDatabase.instance.getDiaryDao().delete(diaryBean)

            GlobalScope.launch(Dispatchers.Main) {
                view.onDiaryDeleted(diaryBean)
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
                view.showDiarySearchResult(diaryList)
            }
        }
    }
}