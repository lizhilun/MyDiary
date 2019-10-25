package com.lizl.mydiary.custom.dialog

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.lizl.mydiary.R
import com.lizl.mydiary.adapter.OperationListAdapter
import com.lizl.mydiary.bean.OperationItem
import kotlinx.android.synthetic.main.dialog_operation_list.*

/**
 * 用于显示操作列表的Dialog
 */
class DialogOperationList(context: Context, private var operationList: List<OperationItem>) : BaseDialog(context)
{
    override fun getDialogContentViewResId() = R.layout.dialog_operation_list

    override fun getDialogWidth() = 0

    override fun initView()
    {
        val operationListAdapter = OperationListAdapter(operationList)
        rv_operation_list.layoutManager = LinearLayoutManager(context)
        rv_operation_list.adapter = operationListAdapter

        operationListAdapter.setOnOperationItemClickListener { dismiss() }
    }
}