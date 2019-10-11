package com.lizl.mydiary.mvp.presenter

import com.lizl.mydiary.bean.BaseDiaryBean
import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.bean.DiaryCategoryBean
import com.lizl.mydiary.mvp.contract.DiaryListFragmentContract
import com.lizl.mydiary.util.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DiaryListFragmentPresenter(private val view: DiaryListFragmentContract.View) : DiaryListFragmentContract.Presenter
{
    override fun loadMoreDiary()
    {
        GlobalScope.launch {
            val diaryList = mutableListOf<BaseDiaryBean>()
            diaryList.add(DiaryCategoryBean("test"))
            val list = AppDatabase.instance.getDiaryDao().getAllDiary()
            if (list.isEmpty())
            {
                for (i in 1..10)
                {
                    val diaryBean = DiaryBean()
                    diaryBean.createTime = System.currentTimeMillis()
                    val text = "12312313213213"
                    for (j in 1 until i)
                    {
                        diaryBean.content += ("$text\n")
                    }
                    diaryBean.content += text
                    list.add(diaryBean)
                }
            }
            diaryList.addAll(list)

            GlobalScope.launch(Dispatchers.Main) {
                view.onMoreDiaries(diaryList, true)
            }
        }
    }
}