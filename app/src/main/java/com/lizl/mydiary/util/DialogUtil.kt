package com.lizl.mydiary.util

import android.content.Context
import com.lizl.mydiary.bean.OperationItem
import com.lizl.mydiary.custom.dialog.BaseDialog
import com.lizl.mydiary.custom.dialog.DialogLoading
import com.lizl.mydiary.custom.dialog.DialogOperationConfirm
import com.lizl.mydiary.custom.dialog.DialogOperationList

class DialogUtil
{
    companion object
    {
        private var dialog: BaseDialog? = null

        fun showLoadingDialog(context: Context, loadingText: String)
        {
            dialog?.dismiss()
            dialog = DialogLoading(context, loadingText)
            dialog?.show()
        }

        fun showOperationConfirmDialog(context: Context, title: String, notify: String, onConfirmBtnClickListener: () -> Unit)
        {
            dialog?.dismiss()
            dialog = DialogOperationConfirm(context, title, notify)
            (dialog as DialogOperationConfirm).setOnConfirmBtnClickListener(onConfirmBtnClickListener)
            dialog?.show()
        }

        fun showOperationListDialog(context: Context, operationList: List<OperationItem>)
        {
            dialog?.dismiss()
            dialog = DialogOperationList(context, operationList)
            dialog?.show()
        }

        fun dismissDialog()
        {
            dialog?.dismiss()
        }
    }
}