package com.lizl.mydiary.adapter

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.mydiary.R
import com.lizl.mydiary.UiApplication
import kotlinx.android.synthetic.main.item_number_key.view.*

class NumberKeyGridAdapter(keyList: List<String>) : BaseQuickAdapter<String, NumberKeyGridAdapter.ViewHolder>(R.layout.item_number_key, keyList.toMutableList())
{

    private var onNumberItemClickListener: ((String) -> Unit)? = null

    override fun convert(helper: ViewHolder, item: String)
    {
        helper.bindViewHolder(item)
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView)
    {
        fun bindViewHolder(keyValue: String)
        {
            if (keyValue == "#")
            {
                itemView.tv_key.visibility = View.GONE
                itemView.iv_key.visibility = View.VISIBLE

                itemView.iv_key.setImageResource(R.drawable.ic_backspace)
            }
            else
            {
                itemView.tv_key.visibility = View.VISIBLE
                itemView.iv_key.visibility = View.GONE
                itemView.tv_key.text = if (keyValue == "*") UiApplication.instance.getText(R.string.exit) else keyValue
            }

            itemView.setOnClickListener { onNumberItemClickListener?.invoke(keyValue) }
        }
    }

    fun setOnNumberItemClickListener(onNumberItemClickListener: (String) -> Unit)
    {
        this.onNumberItemClickListener = onNumberItemClickListener
    }
}