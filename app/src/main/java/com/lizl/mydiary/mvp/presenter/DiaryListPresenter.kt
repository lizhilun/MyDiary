package com.lizl.mydiary.mvp.presenter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.lizl.mydiary.mvp.contract.DiaryListContract
import com.lizl.mydiary.util.AppDatabase

class DiaryListPresenter(private var view: DiaryListContract.View?) : DiaryListContract.Presenter
{
    override fun queryAllDiary(owner: LifecycleOwner)
    {
        AppDatabase.instance.getDiaryDao().getAllDiaryLiveData().observe(owner, Observer {
            view?.onDiariesQueryFinish(it ?: emptyList())
        })
    }

    override fun onDestroy()
    {
        view = null
    }
}