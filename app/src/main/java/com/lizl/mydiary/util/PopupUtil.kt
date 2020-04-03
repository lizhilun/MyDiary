package com.lizl.mydiary.util

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
import com.blankj.utilcode.util.ActivityUtils
import com.lizl.mydiary.R
import com.lizl.mydiary.bean.OperationItem
import com.lizl.mydiary.custom.popup.PopupDiaryTagList
import com.lizl.mydiary.custom.popup.PopupInput
import com.lizl.mydiary.custom.popup.*
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView


object PopupUtil
{
    private var popup: BasePopupView? = null
    private var dialog: Dialog? = null

    fun showLoadingPopup(loadingText: String)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        dismissAll()
        popup = XPopup.Builder(ActivityUtils.getTopActivity()).asCustom(PopupLoading(context, loadingText))
        popup?.show()
    }

    fun showOperationConfirmPopup(title: String, notify: String, onConfirmBtnClickListener: () -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        dismissAll()
        popup = XPopup.Builder(context).asConfirm(title, notify) { onConfirmBtnClickListener.invoke() }.bindLayout(R.layout.popup_operation_confirm)
        popup?.show()
    }

    fun showOperationListPopup(operationList: List<OperationItem>)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        dismissAll()
        popup = XPopup.Builder(context).asCustom(PopupOperationList(context, operationList))
        popup?.show()
    }

    fun showPasswordConfirmPopup(password: String, onInputFinishListener: (String) -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        dismissAll()
        popup = XPopup.Builder(context).asCustom(PopupPassword(context, PopupPassword.PASSWORD_OPERATION_CHECK, password, onInputFinishListener))
        popup?.show()
    }

    fun showSetPasswordPopup(onInputFinishListener: (String) -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        dismissAll()
        popup = XPopup.Builder(context).asCustom(PopupPassword(context, PopupPassword.PASSWORD_OPERATION_NEW, onInputFinishListener = onInputFinishListener))
        popup?.show()
    }

    fun showModifyPasswordPopup(oldPassword: String, onInputFinishListener: (String) -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        dismissAll()
        popup = XPopup.Builder(context).asCustom(PopupPassword(context, PopupPassword.PASSWORD_OPERATION_MODIFY, oldPassword, onInputFinishListener))
        popup?.show()
    }

    fun showInputPasswordPopup(onInputFinishListener: (String) -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        dismissAll()
        popup = XPopup.Builder(context).asCustom(PopupPassword(context, PopupPassword.PASSWORD_OPERATION_INPUT, onInputFinishListener = onInputFinishListener))
        popup?.show()
    }

    fun showRadioGroupPopup(title: String, radioList: List<String>, checkedRadio: String, onSelectFinishListener: (result: String) -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        dismissAll()
        popup = XPopup.Builder(context).asCustom(PopupRadioGroup(context, title, radioList, checkedRadio, onSelectFinishListener))
        popup?.show()
    }

    fun showMoodSelectPopup(withAll: Boolean, onMoodSelectListener: (mood: Int) -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        dismissAll()
        popup = XPopup.Builder(context).asCustom(PopupMoodSelect(context, withAll, onMoodSelectListener))
        popup?.show()
    }

    fun showInputPopup(title: String, defaultValue: String?, editHint: String, inputCompletedCallback: (String) -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        dismissAll()
        popup = XPopup.Builder(context).asCustom(
                PopupInput(context, title, defaultValue, editHint, inputCompletedCallback))
        popup?.show()
    }

    fun showDiaryTagListPopup(onTagSelectFinishListener: (String) -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        dismissAll()
        popup = XPopup.Builder(context).asCustom(PopupDiaryTagList(context, onTagSelectFinishListener))
        popup?.show()
    }

    fun showDatePickerDialog(year: Int, month: Int, day: Int, dateSetListener: (View: DatePicker, Int, Int, Int) -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        dismissAll()
        dialog = DatePickerDialog(context, dateSetListener, year, month, day)
        dialog?.show()
    }

    fun showTimePickerDialog(hour: Int, minute: Int, timeSetListener: (View: TimePicker, Int, Int) -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        dismissAll()
        dialog = TimePickerDialog(context, timeSetListener, hour, minute, true)
        dialog?.show()
    }

    fun dismissAll()
    {
        popup?.dismiss()
        dialog?.dismiss()
    }
}