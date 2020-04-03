package com.lizl.mydiary.util

import com.blankj.utilcode.util.ActivityUtils
import com.lizl.mydiary.R
import com.lizl.mydiary.bean.OperationItem
import com.lizl.mydiary.custom.popup.PopupLoading
import com.lizl.mydiary.custom.popup.PopupOperationList
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView


object PopupUtil
{
    private var popup: BasePopupView? = null

    fun showLoadingPopup(loadingText: String)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        popup?.dismiss()
        popup = XPopup.Builder(ActivityUtils.getTopActivity()).asCustom(PopupLoading(context, loadingText))
        popup?.show()
    }

    fun showOperationConfirmPopup(title: String, notify: String, onConfirmBtnClickListener: () -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        popup?.dismiss()
        popup = XPopup.Builder(context).asConfirm(title, notify) { onConfirmBtnClickListener.invoke() }.bindLayout(R.layout.popup_operation_confirm)
        popup?.show()
    }

    fun showOperationListPopup(operationList: List<OperationItem>)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        popup?.dismiss()
        popup = XPopup.Builder(context).asCustom(PopupOperationList(context, operationList))
        popup?.show()
    }
}