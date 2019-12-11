package com.lizl.mydiary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.lizl.mydiary.R
import com.lizl.mydiary.bean.CountStatisticsBean
import com.lizl.mydiary.util.DiaryUtil
import kotlinx.android.synthetic.main.item_count_statistics.view.*
import java.util.*

class CountStatisticsListAdapter : RecyclerView.Adapter<CountStatisticsListAdapter.ViewHolder>()
{

    private val countStatisticsList = mutableListOf<CountStatisticsBean.BaseCountStatisticsBean>()
    private var maxCount = 0

    private var onItemClickListener: ((CountStatisticsBean.BaseCountStatisticsBean) -> Unit)? = null

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
                is CountStatisticsBean.MoodStatisticsBean ->
                {
                    itemView.iv_statistics.isVisible = true
                    itemView.tv_statistics.isVisible = false
                    itemView.iv_statistics.setImageResource(DiaryUtil.getMoodResByMood(statisticsBean.mood))
                }
                is CountStatisticsBean.TimeStatisticsBean ->
                {
                    itemView.iv_statistics.isVisible = false
                    itemView.tv_statistics.isVisible = true
                    itemView.tv_statistics.text = String.format(Locale.getDefault(), "%02d:00\n%02d:00", statisticsBean.startTime,
                            if (statisticsBean.startTime == 23) 0 else statisticsBean.startTime + 1)
                }
                is CountStatisticsBean.TagStatisticsBean  ->
                {
                    itemView.iv_statistics.isVisible = false
                    itemView.tv_statistics.isVisible = true
                    itemView.tv_statistics.text = statisticsBean.tag
                }
            }

            itemView.npb_statistics.max = maxCount
            itemView.npb_statistics.progress = statisticsBean.count

            itemView.tv_count.text = statisticsBean.count.toString()

            itemView.setOnClickListener { onItemClickListener?.invoke(statisticsBean) }
        }
    }

    fun setOnItemClickListener(onItemClickListener: (CountStatisticsBean.BaseCountStatisticsBean) -> Unit)
    {
        this.onItemClickListener = onItemClickListener
    }
}