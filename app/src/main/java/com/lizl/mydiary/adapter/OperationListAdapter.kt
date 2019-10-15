package com.lizl.mydiary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lizl.mydiary.R
import com.lizl.mydiary.bean.OperationItem
import kotlinx.android.synthetic.main.item_operation.view.*

class OperationListAdapter(private var operationList: List<OperationItem>) : RecyclerView.Adapter<OperationListAdapter.ViewHolder>()
{
    private var onOperationItemClickListener: (() -> Unit)? = null

    override fun getItemCount(): Int = operationList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_operation, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.bindViewHolder(operationList[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        fun bindViewHolder(operationItem: OperationItem)
        {
            itemView.tv_operation_name.text = operationItem.operationName

            itemView.setOnClickListener {
                onOperationItemClickListener?.invoke()
                operationItem.operationItemCallBack.invoke()
            }
        }
    }

    fun setOnOperationItemClickListener(onOperationItemClickListener: () -> Unit)
    {
        this.onOperationItemClickListener = onOperationItemClickListener
    }
}