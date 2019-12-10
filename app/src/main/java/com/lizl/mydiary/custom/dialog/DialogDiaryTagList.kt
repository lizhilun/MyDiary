package com.lizl.mydiary.custom.dialog

import android.content.Context
import android.text.InputFilter
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import com.lizl.mydiary.R
import com.lizl.mydiary.adapter.DiaryTagAdapter
import com.lizl.mydiary.util.DiaryUtil
import com.lizl.mydiary.util.UiUtil
import kotlinx.android.synthetic.main.dialog_diary_tag_list.*

class DialogDiaryTagList(context: Context, private val onTagSelectFinishListener: (String) -> Unit) : BaseDialog(context)
{
    override fun getDialogContentViewResId() = R.layout.dialog_diary_tag_list

    override fun initView()
    {
        val diaryTagAdapter = DiaryTagAdapter(DiaryUtil.getDiaryTagList())
        rv_tag_list.layoutManager = GridLayoutManager(context, 3)
        rv_tag_list.adapter = diaryTagAdapter

        diaryTagAdapter.setOnTagItemClickListener {
            onTagSelectFinishListener.invoke(it)
            dismiss()
        }

        et_new_tag.addTextChangedListener {
            val text = it.toString()
            tv_confirm.isEnabled = text.isNotBlank()
        }

        tv_confirm.setOnClickListener {
            val tagTest = et_new_tag.text.toString()
            DiaryUtil.addDiaryTag(tagTest)
            onTagSelectFinishListener.invoke(tagTest)
            dismiss()
        }

        et_new_tag.filters = arrayOf(InputFilter.LengthFilter(4), UiUtil.getNoWrapOrSpaceFilter())

        tv_cancel.setOnClickListener { dismiss() }

        setOnDismissListener { UiUtil.hideInputKeyboard() }
    }

    override fun getDialogWidth() = (UiUtil.getScreenWidth() * 0.9).toInt()
}