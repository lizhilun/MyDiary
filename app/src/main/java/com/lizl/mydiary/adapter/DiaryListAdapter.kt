package com.lizl.mydiary.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.lizl.mydiary.R
import com.lizl.mydiary.bean.DateBean
import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.mvp.base.BaseActivity
import com.lizl.mydiary.util.UiUtil
import kotlinx.android.synthetic.main.item_diary_list.view.*

class DiaryListAdapter : BaseAdapter<DiaryBean, DiaryListAdapter.ViewHolder>()
{
    override fun getCustomItemViewType(position: Int) = 0

    private var onDiaryItemClickListener: ((DiaryBean) -> Unit)? = null
    private var onDiaryItemLongClickListener: ((DiaryBean) -> Unit)? = null

    override fun createCustomViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_diary_list, parent, false))
    }

    override fun bindCustomViewHolder(holder: ViewHolder, bean: DiaryBean, position: Int)
    {
        holder.bindDiaryViewHolder(bean)
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

            UiUtil.clearTextViewAutoWrap(itemView.tv_diary_content)

            itemView.tv_diary_content.isVisible = !TextUtils.isEmpty(diaryBean.content)

            if (diaryBean.imageList.isNullOrEmpty())
            {
                itemView.rv_image_list.isVisible = false
            }
            else
            {
                itemView.rv_image_list.isVisible = true
                val diaryImageListAdapter = DiaryImageListAdapter(false, 3)
                itemView.rv_image_list.layoutManager = GridLayoutManager(context, 3)
                diaryImageListAdapter.addImageList(diaryBean.imageList!!)
                itemView.rv_image_list.adapter = diaryImageListAdapter

                diaryImageListAdapter.setOnImageClickListener {
                    val imageList = arrayListOf<String>()
                    imageList.addAll(diaryImageListAdapter.getImageList())
                    (itemView.context as BaseActivity<*>).turnToImageBrowserActivity(imageList, it, false)
                }
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

    fun insertDiary(diaryBean: DiaryBean)
    {
        var index = 0
        if (itemCount > 0)
        {
            if (diaryBean.createTime <= getItem(itemCount - 1).createTime)
            {
                index = itemCount
            }
            else for (i in 0 until itemCount - 1)
            {
                if (getItem(i).createTime >= diaryBean.createTime && getItem(i + 1).createTime <= diaryBean.createTime)
                {
                    index = i + 1
                    break
                }
            }
        }
        add(diaryBean, index)
    }
}