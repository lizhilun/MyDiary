package com.lizl.mydiary.custom.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ScrollView
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.lizl.mydiary.R
import com.lizl.mydiary.adapter.DiaryListAdapter
import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.bean.OperationItem
import com.lizl.mydiary.mvp.activity.DiaryContentActivity
import com.lizl.mydiary.util.ActivityUtil
import com.lizl.mydiary.util.AppDatabase
import com.lizl.mydiary.util.DialogUtil
import com.lizl.mydiary.util.FileUtil
import kotlinx.android.synthetic.main.layout_diary_list.view.*
import kotlinx.android.synthetic.main.layout_diary_list_herder.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DiaryListView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : NestedScrollView(context, attrs, defStyleAttr)
{
    private val TAG = "DiaryListView"

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private var diaryListAdapter: DiaryListAdapter

    init
    {
        val diaryListView = LayoutInflater.from(context).inflate(R.layout.layout_diary_list, this, false)
        addView(diaryListView)

        overScrollMode = OVER_SCROLL_NEVER

        diaryListAdapter = DiaryListAdapter()
        rv_diary_list.layoutManager = LinearLayoutManager(context)
        rv_diary_list.adapter = diaryListAdapter

        diaryListAdapter.setOnDiaryItemClickListener { ActivityUtil.turnToActivity(DiaryContentActivity::class.java, it) }

        diaryListAdapter.setOnDiaryItemLongClickListener { showDiaryOperationListDialog(it) }
    }

    fun showDiaryList(diaryList: List<DiaryBean>)
    {
        diaryListAdapter.clear()
        diaryListAdapter.addAll(diaryList)
        updateDiaryListHeader()
    }

    fun onDiarySaveSuccess(diaryBean: DiaryBean)
    {
        val findDiaryBean = diaryListAdapter.getData().find { it.uid == diaryBean.uid }
        if (findDiaryBean != null)
        {
            onDiaryDelete(findDiaryBean)
        }
        onDiaryInsert(diaryBean)
    }

    private fun onDiaryInsert(diaryBean: DiaryBean)
    {
        diaryListAdapter.insertDiary(diaryBean)
        updateDiaryListHeader()
    }

    private fun onDiaryDelete(diaryBean: DiaryBean)
    {
        diaryListAdapter.remove(diaryBean)
        updateDiaryListHeader()
    }

    private fun updateDiaryListHeader()
    {
        tv_end_footer.isVisible = diaryListAdapter.getData().isNotEmpty()
        layout_diary_header.isVisible = diaryListAdapter.getData().isNotEmpty()
        tv_header_content.text = context.getString(R.string.diary_total_count, diaryListAdapter.getData().size)
    }

    private fun showDiaryOperationListDialog(diaryBean: DiaryBean)
    {
        val operationList = mutableListOf<OperationItem>()
        operationList.add(OperationItem(context.getString(R.string.delete)) { deleteDiary(diaryBean) })
        DialogUtil.showOperationListDialog(context, operationList)
    }

    private fun deleteDiary(diaryBean: DiaryBean)
    {
        GlobalScope.launch {
            diaryBean.imageList?.forEach { FileUtil.deleteFile(it) }
            AppDatabase.instance.getDiaryDao().delete(diaryBean)

            GlobalScope.launch(Dispatchers.Main) { onDiaryDelete(diaryBean) }
        }
    }
}