package com.lizl.mydiary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lizl.mydiary.R
import kotlinx.android.synthetic.main.item_mood.view.*

class MoodListAdapter : RecyclerView.Adapter<MoodListAdapter.ViewHolder>()
{
    private val moodResList = mutableListOf<Int>()

    private var onMoodItemClickListener: ((moodRedId: Int) -> Unit)? = null

    fun setData(moodResList: List<Int>)
    {
        this.moodResList.clear()
        this.moodResList.addAll(moodResList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_mood, parent, false))
    }

    override fun getItemCount() = moodResList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.bindViewHolder(moodResList[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
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