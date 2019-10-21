package com.lizl.mydiary.util

import android.content.Context
import com.lizl.mydiary.bean.OperationItem
import com.lizl.mydiary.custom.dialog.*

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

        fun showPasswordConfirmDialog(context: Context, password: String, onInputFinishListener: (String) -> Unit)
        {
            dialog?.dismiss()
            dialog = DialogPassword(context, password, true, onInputFinishListener)
            dialog?.show()
        }

        fun showSetPasswordDialog(context: Context, onInputFinishListener: (String) -> Unit)
        {
            dialog?.dismiss()
            dialog = DialogPassword(context, onInputFinishListener)
            dialog?.show()
        }

        fun showModifyPasswordDialog(context: Context, oldPassword: String, onInputFinishListener: (String) -> Unit)
        {
            dialog?.dismiss()
            dialog = DialogPassword(context, oldPassword, false, onInputFinishListener)
            dialog?.show()
        }

        fun showRadioGroupDialog(context: Context, title: String, radioList: List<String>, checkedRadio: String,
                                 onSelectFinishListener: (result: String) -> Unit)
        {
            dialog?.dismiss()
            dialog = DialogRadioGroup(context, title, radioList, checkedRadio, onSelectFinishListener)
            dialog?.show()
        }

        fun dismissDialog()
        {
            dialog?.dismiss()
        }
    }
}