package com.lizl.mydiary.adapter

import android.view.View
import androidx.core.view.isVisible
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.mydiary.R
import com.lizl.mydiary.bean.CountStatisticsBean
import com.lizl.mydiary.util.DiaryUtil
import com.lizl.mydiary.util.SkinUtil
import kotlinx.android.synthetic.main.item_count_statistics.view.*
import java.util.*

class CountStatisticsListAdapter : BaseQuickAdapter<CountStatisticsBean.BaseCountStatisticsBean
        , CountStatisticsListAdapter.ViewHolder>(R.layout.item_count_statistics)
{

    private var maxCount = 0

    private var onItemClickListener: ((CountStatisticsBean.BaseCountStatisticsBean) -> Unit)? = null

    fun setData(countStatisticsList: List<CountStatisticsBean.BaseCountStatisticsBean>)
    {
        maxCount = countStatisticsList.maxBy { it.count }?.count ?: 0
        setNewData(countStatisticsList.toMutableList())
    }

    override fun convert(helper: ViewHolder, item: CountStatisticsBean.BaseCountStatisticsBean)
    {
        helper.bindViewHolder(item)
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView)
    {
        fun bindViewHolder(statisticsBean: CountStatisticsBean.BaseCountStatisticsBean)
        {
            when (statisticsBean)
            {
                is CountStatisticsBean.MoodStatisticsBean ->
                {
                    itemView.iv_statistics.isVisible = true
                    itemView.tv_statistics.isVisible = false
                    val moodDrawable = itemView.context.getDrawable(DiaryUtil.getMoodResByMood(statisticsBean.mood))
                    moodDrawable?.setTint(SkinUtil.getGlobalTextColor())
                    itemView.iv_statistics.setImageDrawable(moodDrawable)
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