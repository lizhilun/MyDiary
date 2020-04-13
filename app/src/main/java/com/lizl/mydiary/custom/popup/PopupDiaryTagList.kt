package com.lizl.mydiary.custom.popup

import android.content.Context
import android.text.InputFilter
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import com.lizl.mydiary.R
import com.lizl.mydiary.adapter.DiaryTagAdapter
import com.lizl.mydiary.util.DiaryUtil
import com.lizl.mydiary.util.UiUtil
import com.lxj.xpopup.core.CenterPopupView
import kotlinx.android.synthetic.main.popup_diary_tag_list.view.*

class PopupDiaryTagList(context: Context, private val onTagSelectFinishListener: (String) -> Unit) : CenterPopupView(context)
{

    override fun getImplLayoutId() = R.layout.popup_diary_tag_list

    override fun onCreate()
    {
        val diaryTagAdapter = DiaryTagAdapter(DiaryUtil.getDiaryTagList())
        rv_tag_list.layoutManager = GridLayoutManager(context, 3)
        rv_tag_list.adapter = diaryTagAdapter

        diaryTagAdapter.setOnTagItemClickListener {
            onTagSelectFinishListener.invoke(it)
            dismiss()
        }

        tv_confirm.isEnabled = false

        et_new_tag.addTextChangedListener {
            tv_confirm.isEnabled = it.toString().isNotBlank()
        }

        et_new_tag.filters = arrayOf(InputFilter.LengthFilter(4), UiUtil.getNoWrapOrSpaceFilter())

        tv_cancel.setOnClickListener { dismiss() }

        tv_confirm.setOnClickListener {
            val tagTest = et_new_tag.text.toString()
            DiaryUtil.addDiaryTag(tagTest)
            onTagSelectFinishListener.invoke(tagTest)
            dismiss()
        }
    }

    override fun onDismiss()
    {
        UiUtil.hideInputKeyboard()
    }
}