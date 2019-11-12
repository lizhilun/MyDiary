package com.lizl.mydiary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.numberprogressbar.NumberProgressBar
import com.lizl.mydiary.R
import com.lizl.mydiary.bean.MoodStatisticsBean
import com.lizl.mydiary.util.DiaryUtil
import kotlinx.android.synthetic.main.item_mood_statistics.view.*

class MoodStatisticsListAdapter : RecyclerView.Adapter<MoodStatisticsListAdapter.ViewHolder>()
{

    private val moodStatisticsList = mutableListOf<MoodStatisticsBean>()
    private var maxCount = 0

    fun setData(moodStatisticsList: List<MoodStatisticsBean>)
    {
        this.moodStatisticsList.clear()
        this.moodStatisticsList.addAll(moodStatisticsList)
        maxCount = moodStatisticsList.maxBy { it.diaryCount }?.diaryCount ?: 0
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_mood_statistics, parent, false))
    }

    override fun getItemCount() = moodStatisticsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.bindViewHolder(moodStatisticsList[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        fun bindViewHolder(moodStatisticsBean: MoodStatisticsBean)
        {
            itemView.iv_mood.setImageResource(DiaryUtil.instance.getMoodResByMood(moodStatisticsBean.mood))

            val barHeight = itemView.context.resources.getDimension(R.dimen.mood_statistics_progress_bar_height)
            itemView.npb_statistics.max = maxCount
            itemView.npb_statistics.progress = moodStatisticsBean.diaryCount
            itemView.npb_statistics.reachedBarColor = ContextCompat.getColor(itemView.context, R.color.colorPrimary)
            itemView.npb_statistics.reachedBarHeight = barHeight
            itemView.npb_statistics.unreachedBarHeight = barHeight
            itemView.npb_statistics.unreachedBarColor = ContextCompat.getColor(itemView.context, R.color.transparent)
            itemView.npb_statistics.setProgressTextVisibility(NumberProgressBar.ProgressTextVisibility.Invisible)

            itemView.tv_count.text = moodStatisticsBean.diaryCount.toString()
        }
    }
}