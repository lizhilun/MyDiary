package com.lizl.mydiary.util

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.ImageView
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
    private var dialog: Dialog? = null
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
        showPopup(XPopup.Builder(context).asCustom(PopupPassword(context, PopupPassword.PASSWORD_OPERATION_NEW, onInputFinishListener = onInputFinishListener)))
    }

    fun showModifyPasswordPopup(oldPassword: String, onInputFinishListener: (String) -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        showPopup(XPopup.Builder(context).asCustom(PopupPassword(context, PopupPassword.PASSWORD_OPERATION_MODIFY, oldPassword, onInputFinishListener)))
    }

    fun showInputPasswordPopup(onInputFinishListener: (String) -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        showPopup(
                XPopup.Builder(context).asCustom(PopupPassword(context, PopupPassword.PASSWORD_OPERATION_INPUT, onInputFinishListener = onInputFinishListener)))
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

    fun showImageViewerPopup(imageView: ImageView, showImage: String, imageList: List<String>)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        showPopup(XPopup.Builder(context).asCustom(PopupImageViewer(context).apply {
            setSrcView(imageView, imageList.indexOf(showImage))
            setImageUrls(imageList.toMutableList())
        }))
    }

    fun dismissAll()
    {
        GlobalScope.launch(Dispatchers.Main) {
            showPopupJob?.cancel()
            curPopup?.dismiss()
            dialog?.dismiss()
        }
    }

    private fun showPopup(popup: BasePopupView)
    {
        GlobalScope.launch(Dispatchers.Main) {
            dialog?.dismiss()
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
}