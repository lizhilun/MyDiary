package com.lizl.mydiary.adapter

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.mydiary.R
import kotlinx.android.synthetic.main.item_diary_tag.view.*

class DiaryTagAdapter(tagList: List<String>) : BaseQuickAdapter<String, DiaryTagAdapter.ViewHolder>(R.layout.item_diary_tag, tagList.toMutableList())
{

    private var onTagItemClickListener: ((String) -> Unit)? = null

    override fun convert(helper: ViewHolder, item: String)
    {
        helper.bindViewHolder(item)
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView)
    {
        fun bindViewHolder(tag: String)
        {
            itemView.tv_tag.text = "#$tag#"

            itemView.setOnClickListener { onTagItemClickListener?.invoke(tag) }
        }
    }

    fun setOnTagItemClickListener(onTagItemClickListener: (String) -> Unit)
    {
        this.onTagItemClickListener = onTagItemClickListener
    }
}