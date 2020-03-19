package com.lizl.mydiary.custom.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.view.isInvisible
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.animation.SlideInRightAnimation
import com.lizl.mydiary.R
import com.lizl.mydiary.adapter.DiaryListAdapter
import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.bean.OperationItem
import com.lizl.mydiary.custom.others.CustomDiffUtil
import com.lizl.mydiary.mvp.activity.DiaryContentActivity
import com.lizl.mydiary.util.ActivityUtil
import com.lizl.mydiary.util.AppDatabase
import com.lizl.mydiary.util.DialogUtil
import com.lizl.mydiary.util.FileUtil
import isDiarySameContent
import kotlinx.android.synthetic.main.layout_diary_list.view.*
import kotlinx.android.synthetic.main.layout_diary_list_herder.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DiaryListView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : NestedScrollView(context, attrs, defStyleAttr)
{
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

        val callback =
            CustomDiffUtil<DiaryBean>({ oldItem, newItem -> oldItem.uid == newItem.uid }, { oldItem, newItem -> oldItem.isDiarySameContent(newItem) })

        diaryListAdapter.adapterAnimation = SlideInRightAnimation()
        diaryListAdapter.setDiffCallback(callback)

        diaryListAdapter.setOnDiaryItemClickListener { ActivityUtil.turnToActivity(DiaryContentActivity::class.java, it) }

        diaryListAdapter.setOnDiaryItemLongClickListener { showDiaryOperationListDialog(it) }
    }

    fun showDiaryList(diaryList: List<DiaryBean>)
    {
        diaryListAdapter.setDiffNewData(diaryList.toMutableList())
        updateDiaryListHeader(diaryList.size)
    }

    private fun updateDiaryListHeader(dataSize: Int)
    {
        tv_end_footer.isInvisible = dataSize == 0
        layout_diary_header.isInvisible = dataSize == 0
        tv_header_content.text = context.getString(R.string.diary_total_count, dataSize)
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
        }
    }
}