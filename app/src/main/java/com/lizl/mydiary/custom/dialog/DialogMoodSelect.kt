package com.lizl.mydiary.custom.dialog

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import com.lizl.mydiary.R
import com.lizl.mydiary.adapter.MoodListAdapter
import com.lizl.mydiary.util.DiaryUtil
import kotlinx.android.synthetic.main.dialog_mood_select.*

class DialogMoodSelect(context: Context, private val onMoodSelectListener: (mood: Int) -> Unit) : BaseDialog(context)
{
    override fun getDialogContentViewResId() = R.layout.dialog_mood_select

    override fun initView()
    {
        rv_mood_grid.layoutManager = GridLayoutManager(context, 3)
        val moodListAdapter = MoodListAdapter()
        rv_mood_grid.adapter = moodListAdapter

        moodListAdapter.setData(DiaryUtil.instance.getMoodResList())

        moodListAdapter.setOnMoodItemClickListener {
            onMoodSelectListener.invoke(DiaryUtil.instance.getMoodByMoodRes(it))
            dismiss()
        }
    }

    override fun getDialogWidth() = context.resources.getDimensionPixelOffset(R.dimen.dialog_mood_select_width)
}