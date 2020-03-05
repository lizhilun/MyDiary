package com.lizl.mydiary.adapter

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.mydiary.R
import kotlinx.android.synthetic.main.item_mood.view.*

class MoodListAdapter : BaseQuickAdapter<Int, MoodListAdapter.ViewHolder>(R.layout.item_mood)
{
    private var onMoodItemClickListener: ((moodRedId: Int) -> Unit)? = null

    override fun convert(helper: ViewHolder, item: Int)
    {
        helper.bindViewHolder(item)
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView)
    {
        fun bindViewHolder(moodResId: Int)
        {
            itemView.iv_mood.setImageResource(moodResId)

            itemView.setOnClickListener { onMoodItemClickListener?.invoke(moodResId) }
        }
    }

    fun setOnMoodItemClickListener(onMoodItemClickListener: (moodRedId: Int) -> Unit)
    {
        this.onMoodItemClickListener = onMoodItemClickListener
    }
}