package com.lizl.mydiary.custom.dialog

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.lizl.mydiary.R
import com.lizl.mydiary.adapter.OperationListAdapter
import com.lizl.mydiary.bean.OperationItem
import kotlinx.android.synthetic.main.dialog_operation_list.*

/**
 * 用于显示操作列表的Dialog
 */
class DialogOperationList(context: Context, private var operationList: List<OperationItem>) : BaseDialog(context, null)
{
    override fun getDialogContentView(): View = layoutInflater.inflate(R.layout.dialog_operation_list, null)

    override fun getDialogWidth() = 0

    override fun initView()
    {
        val operationListAdapter = OperationListAdapter(operationList)
        rv_operation_list.layoutManager = LinearLayoutManager(context)
        rv_operation_list.adapter = operationListAdapter

        operationListAdapter.setOnOperationItemClickListener { dismiss() }
    }

    override fun onConfirmBtnClick() = true
}