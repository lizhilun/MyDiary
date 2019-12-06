package com.lizl.mydiary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lizl.mydiary.R
import kotlinx.android.synthetic.main.item_diary_tag.view.*

class DiaryTagAdapter(private val tagList: List<String>) : RecyclerView.Adapter<DiaryTagAdapter.ViewHolder>()
{

    private var onTagItemClickListener: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_diary_tag, parent, false))
    }

    override fun getItemCount() = tagList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.bindViewHolder(tagList[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
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