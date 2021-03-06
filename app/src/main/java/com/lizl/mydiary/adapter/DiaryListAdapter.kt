package com.lizl.mydiary.adapter

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.mydiary.R
import com.lizl.mydiary.bean.DateBean
import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.util.DiaryUtil
import com.lizl.mydiary.util.UiUtil
import kotlinx.android.synthetic.main.item_diary_list.view.*

class DiaryListAdapter : BaseQuickAdapter<DiaryBean, DiaryListAdapter.ViewHolder>(R.layout.item_diary_list)
{
    private var onDiaryItemClickListener: ((DiaryBean) -> Unit)? = null
    private var onDiaryItemLongClickListener: ((DiaryBean) -> Unit)? = null

    override fun convert(helper: ViewHolder, item: DiaryBean)
    {
        helper.bindDiaryViewHolder(item)
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView)
    {
        fun bindDiaryViewHolder(diaryBean: DiaryBean)
        {
            val dateBean = DateBean(diaryBean.createTime)
            itemView.tv_day.text = dateBean.day.toString()
            itemView.tv_week.text = dateBean.week
            itemView.tv_year_and_month.text = "${dateBean.year}.${dateBean.month}"
            itemView.tv_diary_content.text = diaryBean.content
            itemView.tv_hour_and_minute.text = dateBean.getHourAndMinute()
            itemView.tv_word_count.text = "${DiaryUtil.sumStringWord(diaryBean.content)}${context.getString(R.string.word)}"

            UiUtil.clearTextViewAutoWrap(itemView.tv_diary_content)

            itemView.tv_diary_content.isVisible = diaryBean.content.isNotEmpty()

            if (diaryBean.imageList.isNullOrEmpty())
            {
                itemView.rv_image_list.isVisible = false
            }
            else
            {
                itemView.rv_image_list.isVisible = true
                val diaryImageListAdapter = DiaryImageListAdapter(false, 3, false)
                itemView.rv_image_list.layoutManager = GridLayoutManager(context, 3)
                diaryImageListAdapter.addImageList(diaryBean.imageList!!)
                itemView.rv_image_list.adapter = diaryImageListAdapter
            }

            itemView.rv_image_list.setOnEmptyClickListener { onDiaryItemClickListener?.invoke(diaryBean) }

            itemView.rv_image_list.setOnEmptyLongClickListener { onDiaryItemLongClickListener?.invoke(diaryBean) }

            itemView.setOnClickListener { onDiaryItemClickListener?.invoke(diaryBean) }

            itemView.setOnLongClickListener {
                onDiaryItemLongClickListener?.invoke(diaryBean)
                true
            }
        }
    }

    fun setOnDiaryItemClickListener(onDiaryItemClickListener: (DiaryBean) -> Unit)
    {
        this.onDiaryItemClickListener = onDiaryItemClickListener
    }

    fun setOnDiaryItemLongClickListener(onDiaryItemLongClickListener: (DiaryBean) -> Unit)
    {
        this.onDiaryItemLongClickListener = onDiaryItemLongClickListener
    }
}