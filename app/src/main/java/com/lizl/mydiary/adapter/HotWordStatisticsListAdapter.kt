package com.lizl.mydiary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.numberprogressbar.NumberProgressBar
import com.lizl.mydiary.R
import com.lizl.mydiary.bean.HotWordBean
import kotlinx.android.synthetic.main.item_hot_word_statistics.view.*

class HotWordStatisticsListAdapter : RecyclerView.Adapter<HotWordStatisticsListAdapter.ViewHolder>()
{

    private val hotWordStatisticsList = mutableListOf<HotWordBean>()
    private var maxCount = 0

    private var onHotWordItemClickListener: ((hotWordBean: HotWordBean) -> Unit)? = null

    private var onHotWordItemLongClickListener: ((hotWordBean: HotWordBean) -> Unit)? = null

    fun setData(hotWordStatisticsList: List<HotWordBean>)
    {
        this.hotWordStatisticsList.clear()
        this.hotWordStatisticsList.addAll(hotWordStatisticsList)
        maxCount = hotWordStatisticsList.maxBy { it.frequency }?.frequency ?: 0
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_hot_word_statistics, parent, false))
    }

    override fun getItemCount() = hotWordStatisticsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.bindViewHolder(hotWordStatisticsList[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        fun bindViewHolder(hotWordBean: HotWordBean)
        {
            itemView.tv_word.text = hotWordBean.word

            val barHeight = itemView.context.resources.getDimension(R.dimen.mood_statistics_progress_bar_height)
            itemView.npb_statistics.max = maxCount
            itemView.npb_statistics.progress = hotWordBean.frequency
            itemView.npb_statistics.reachedBarColor = ContextCompat.getColor(itemView.context, R.color.colorPrimary)
            itemView.npb_statistics.reachedBarHeight = barHeight
            itemView.npb_statistics.unreachedBarHeight = barHeight
            itemView.npb_statistics.unreachedBarColor = ContextCompat.getColor(itemView.context, R.color.transparent)
            itemView.npb_statistics.setProgressTextVisibility(NumberProgressBar.ProgressTextVisibility.Invisible)

            itemView.tv_count.text = hotWordBean.frequency.toString()

            itemView.setOnClickListener { onHotWordItemClickListener?.invoke(hotWordBean) }

            itemView.setOnLongClickListener {
                onHotWordItemLongClickListener?.invoke(hotWordBean)
                true
            }
        }
    }

    fun setOnWordItemClickListener(onHotWordItemClickListener: (hotWordBean: HotWordBean) -> Unit)
    {
        this.onHotWordItemClickListener = onHotWordItemClickListener
    }

    fun setOnWordItemLongClickListener(onHotWordItemLongClickListener: (hotWordBean: HotWordBean) -> Unit)
    {
        this.onHotWordItemLongClickListener = onHotWordItemLongClickListener
    }
}