package com.lizl.mydiary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lizl.mydiary.R
import com.lizl.mydiary.UiApplication
import kotlinx.android.synthetic.main.item_number_key.view.*

class NumberKeyGridAdapter(private val keyList: List<String>) : RecyclerView.Adapter<NumberKeyGridAdapter.ViewHolder>()
{
    private var onNumberItemClickListener: ((String) -> Unit)? = null

    override fun getItemCount(): Int = keyList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_number_key, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.bindViewHolder(keyList[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
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