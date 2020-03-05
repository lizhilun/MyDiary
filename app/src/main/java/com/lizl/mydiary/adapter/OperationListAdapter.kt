package com.lizl.mydiary.adapter

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.mydiary.R
import com.lizl.mydiary.bean.OperationItem
import kotlinx.android.synthetic.main.item_operation.view.*

class OperationListAdapter(operationList: List<OperationItem>) :
        BaseQuickAdapter<OperationItem, OperationListAdapter.ViewHolder>(R.layout.item_operation, operationList.toMutableList())
{
    private var onOperationItemClickListener: (() -> Unit)? = null

    override fun convert(helper: ViewHolder, item: OperationItem)
    {
        helper.bindViewHolder(item)
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView)
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