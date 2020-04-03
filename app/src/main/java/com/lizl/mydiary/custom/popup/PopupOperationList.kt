package com.lizl.mydiary.custom.popup

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.lizl.mydiary.R
import com.lizl.mydiary.adapter.OperationListAdapter
import com.lizl.mydiary.bean.OperationItem
import com.lxj.xpopup.core.CenterPopupView
import kotlinx.android.synthetic.main.popup_operation_list.view.*

class PopupOperationList(context: Context, private var operationList: List<OperationItem>) : CenterPopupView(context)
{
    override fun getImplLayoutId() = R.layout.popup_operation_list

    override fun onCreate()
    {
        val operationListAdapter = OperationListAdapter(operationList)
        rv_operation_list.layoutManager = LinearLayoutManager(context)
        rv_operation_list.adapter = operationListAdapter

        operationListAdapter.setOnOperationItemClickListener { dismiss() }
    }
}