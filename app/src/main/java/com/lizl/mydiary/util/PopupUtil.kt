package com.lizl.mydiary.util

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
import com.blankj.utilcode.util.ActivityUtils
import com.lizl.mydiary.R
import com.lizl.mydiary.bean.OperationItem
import com.lizl.mydiary.custom.popup.*
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import kotlinx.coroutines.*


object PopupUtil
{
    private var curPopup: BasePopupView? = null
    private var curDialog: Dialog? = null
    private var showPopupJob: Job? = null

    fun showLoadingPopup(loadingText: String)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        showPopup(XPopup.Builder(ActivityUtils.getTopActivity()).asCustom(PopupLoading(context, loadingText)))
    }

    fun showOperationConfirmPopup(title: String, notify: String, onConfirmBtnClickListener: () -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        showPopup(XPopup.Builder(context).asConfirm(title, notify) { onConfirmBtnClickListener.invoke() }.bindLayout(R.layout.popup_operation_confirm))
    }

    fun showOperationListPopup(operationList: List<OperationItem>)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        showPopup(XPopup.Builder(context).asCustom(PopupOperationList(context, operationList)))
    }

    fun showPasswordConfirmPopup(password: String, onInputFinishListener: (String) -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        showPopup(XPopup.Builder(context).asCustom(PopupPassword(context, PopupPassword.PASSWORD_OPERATION_CHECK, password, onInputFinishListener)))
    }

    fun showSetPasswordPopup(onInputFinishListener: (String) -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        showPopup(XPopup.Builder(context).asCustom(PopupPassword(context, PopupPassword.PASSWORD_OPERATION_NEW, null, onInputFinishListener)))
    }

    fun showModifyPasswordPopup(oldPassword: String, onInputFinishListener: (String) -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        showPopup(XPopup.Builder(context).asCustom(PopupPassword(context, PopupPassword.PASSWORD_OPERATION_MODIFY, oldPassword, onInputFinishListener)))
    }

    fun showInputPasswordPopup(onInputFinishListener: (String) -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        showPopup(XPopup.Builder(context).asCustom(PopupPassword(context, PopupPassword.PASSWORD_OPERATION_INPUT, null, onInputFinishListener)))
    }

    fun showRadioGroupPopup(title: String, radioList: List<String>, checkedRadio: String, onSelectFinishListener: (result: String) -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        showPopup(XPopup.Builder(context).asCustom(PopupRadioGroup(context, title, radioList, checkedRadio, onSelectFinishListener)))
    }

    fun showMoodSelectPopup(withAll: Boolean, onMoodSelectListener: (mood: Int) -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        showPopup(XPopup.Builder(context).asCustom(PopupMoodSelect(context, withAll, onMoodSelectListener)))
    }

    fun showInputPopup(title: String, defaultValue: String?, editHint: String, inputCompletedCallback: (String) -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        showPopup(XPopup.Builder(context).asCustom(PopupInput(context, title, defaultValue, editHint, inputCompletedCallback)))
    }

    fun showDiaryTagListPopup(onTagSelectFinishListener: (String) -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        showPopup(XPopup.Builder(context).asCustom(PopupDiaryTagList(context, onTagSelectFinishListener)))
    }

    fun showDatePickerDialog(year: Int, month: Int, day: Int, dateSetListener: (View: DatePicker, Int, Int, Int) -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        showDialog(DatePickerDialog(context, dateSetListener, year, month, day))
    }

    fun showTimePickerDialog(hour: Int, minute: Int, timeSetListener: (View: TimePicker, Int, Int) -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        showDialog(TimePickerDialog(context, timeSetListener, hour, minute, true))
    }

    fun showImageViewerPopup(showImage: String, imageList: MutableList<String>, showDeleteBtn: Boolean)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        showPopup(XPopup.Builder(context).asCustom(PopupImageViewer(context, imageList, imageList.indexOf(showImage), showDeleteBtn)))
    }

    fun dismissAll()
    {
        GlobalScope.launch(Dispatchers.Main) {
            showPopupJob?.cancel()
            curPopup?.dismiss()
            curDialog?.dismiss()
        }
    }

    private fun showPopup(popup: BasePopupView)
    {
        GlobalScope.launch(Dispatchers.Main) {
            curDialog?.dismiss()
            showPopupJob?.cancel()
            showPopupJob = GlobalScope.launch(Dispatchers.Main) {
                if (curPopup?.isShow == true)
                {
                    curPopup?.dismiss()
                    delay(300)
                }
                curPopup = popup
                curPopup?.show()
            }
        }
    }

    private fun showDialog(dialog: Dialog)
    {
        GlobalScope.launch(Dispatchers.Main) {
            showPopupJob?.cancel()
            curPopup?.dismiss()
            curDialog?.dismiss()

            curDialog = dialog
            curDialog?.show()
        }
    }
}