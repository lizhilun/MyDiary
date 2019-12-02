package com.lizl.mydiary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.numberprogressbar.NumberProgressBar
import com.lizl.mydiary.R
import com.lizl.mydiary.bean.CountStatisticsBean
import com.lizl.mydiary.util.DiaryUtil
import kotlinx.android.synthetic.main.item_count_statistics.view.*

class CountStatisticsListAdapter : RecyclerView.Adapter<CountStatisticsListAdapter.ViewHolder>()
{

    private val countStatisticsList = mutableListOf<CountStatisticsBean.BaseCountStatisticsBean>()
    private var maxCount = 0

    private var onItemClickListener: ((CountStatisticsBean.BaseCountStatisticsBean) -> Unit)? = null
    private var onItemLongClickListener: ((CountStatisticsBean.BaseCountStatisticsBean) -> Unit)? = null

    fun setData(countStatisticsList: List<CountStatisticsBean.BaseCountStatisticsBean>)
    {
        this.countStatisticsList.clear()
        this.countStatisticsList.addAll(countStatisticsList)
        maxCount = countStatisticsList.maxBy { it.count }?.count ?: 0
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_count_statistics, parent, false))
    }

    override fun getItemCount() = countStatisticsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.bindViewHolder(countStatisticsList[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        fun bindViewHolder(statisticsBean: CountStatisticsBean.BaseCountStatisticsBean)
        {
            when (statisticsBean)
            {
                is CountStatisticsBean.MoodStatisticsBean    ->
                {
                    itemView.iv_mood.isVisible = true
                    itemView.tv_word.isVisible = false
                    itemView.iv_mood.setImageResource(DiaryUtil.getMoodResByMood(statisticsBean.mood))
                }
                is CountStatisticsBean.HotWordStatisticsBean ->
                {
                    itemView.iv_mood.isVisible = false
                    itemView.tv_word.isVisible = true
                    itemView.tv_word.text = statisticsBean.word
                }
            }

            val barHeight = itemView.context.resources.getDimension(R.dimen.mood_statistics_progress_bar_height)
            itemView.npb_statistics.max = maxCount
            itemView.npb_statistics.progress = statisticsBean.count
            itemView.npb_statistics.reachedBarColor = ContextCompat.getColor(itemView.context, R.color.colorPrimary)
            itemView.npb_statistics.reachedBarHeight = barHeight
            itemView.npb_statistics.unreachedBarHeight = barHeight
            itemView.npb_statistics.unreachedBarColor = ContextCompat.getColor(itemView.context, R.color.transparent)
            itemView.npb_statistics.setProgressTextVisibility(NumberProgressBar.ProgressTextVisibility.Invisible)

            itemView.tv_count.text = statisticsBean.count.toString()

            itemView.setOnClickListener { onItemClickListener?.invoke(statisticsBean) }

            itemView.setOnLongClickListener {
                onItemLongClickListener?.invoke(statisticsBean)
                true
            }
        }
    }

    fun setOnItemClickListener(onItemClickListener: (CountStatisticsBean.BaseCountStatisticsBean) -> Unit)
    {
        this.onItemClickListener = onItemClickListener
    }

    fun setOnItemLongClickListener(onItemLongClickListener: (CountStatisticsBean.BaseCountStatisticsBean) -> Unit)
    {
        this.onItemLongClickListener = onItemLongClickListener
    }
}