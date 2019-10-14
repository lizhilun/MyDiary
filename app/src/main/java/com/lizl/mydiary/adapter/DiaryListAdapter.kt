package com.lizl.mydiary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.lizl.mydiary.R
import com.lizl.mydiary.bean.BaseDiaryBean
import com.lizl.mydiary.bean.DateBean
import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.bean.DiaryCategoryBean
import kotlinx.android.synthetic.main.item_diary_category.view.*
import kotlinx.android.synthetic.main.item_diary_list.view.*

class DiaryListAdapter : BaseAdapter<BaseDiaryBean, DiaryListAdapter.ViewHolder>()
{
    private val DIARY_TYPE_DIARY = 1
    private val DIARY_TYPE_CATEGORY = 2

    private var onDiaryItemClick: ((DiaryBean) -> Unit)? = null

    override fun createCustomViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return when (viewType)
        {
            DIARY_TYPE_DIARY -> ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_diary_list, parent, false))
            else             -> ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_diary_category, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int
    {
        if (super.getItemViewType(position) == VIEW_TYPE_FOOTER)
        {
            return VIEW_TYPE_FOOTER
        }
        return when (getItem(position))
        {
            is DiaryBean         -> DIARY_TYPE_DIARY
            is DiaryCategoryBean -> DIARY_TYPE_CATEGORY
            else                 -> super.getItemViewType(position)
        }
    }

    override fun bindCustomViewHolder(holder: ViewHolder, bean: BaseDiaryBean, position: Int)
    {
        when (getItemViewType(position))
        {
            DIARY_TYPE_DIARY    -> holder.bindDiaryViewHolder(bean as DiaryBean)
            DIARY_TYPE_CATEGORY -> holder.bindCategoryViewHolder(bean as DiaryCategoryBean)
        }

    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView)
    {
        fun bindDiaryViewHolder(diaryBean: DiaryBean)
        {
            val dateBean = DateBean(diaryBean.createTime)
            itemView.tv_day.text = dateBean.day.toString()
            itemView.tv_month.text = "${dateBean.month}/${dateBean.week}"
            itemView.tv_diary_content.text = diaryBean.content

            if (diaryBean.imageList.isNullOrEmpty())
            {
                itemView.rv_image_list.visibility = View.GONE
            }
            else
            {
                itemView.rv_image_list.visibility = View.VISIBLE
                val diaryImageListAdapter = DiaryImageListAdapter(false, 3)
                itemView.rv_image_list.layoutManager = GridLayoutManager(context, 3)
                diaryImageListAdapter.addImageList(diaryBean.imageList!!)
                itemView.rv_image_list.adapter = diaryImageListAdapter
            }

            itemView.rv_image_list.setOnClickListener {
                onDiaryItemClick?.invoke(diaryBean)
            }

            itemView.setOnClickListener {
                onDiaryItemClick?.invoke(diaryBean)
            }
        }

        fun bindCategoryViewHolder(diaryCategoryBean: DiaryCategoryBean)
        {
            itemView.tv_category_content.text = diaryCategoryBean.content
        }
    }

    fun setOnDiaryItemClick(onDiaryItemClick: (DiaryBean) -> Unit)
    {
        this.onDiaryItemClick = onDiaryItemClick
    }

}