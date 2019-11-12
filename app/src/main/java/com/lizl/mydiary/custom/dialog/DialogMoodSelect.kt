package com.lizl.mydiary.custom.dialog

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import com.lizl.mydiary.R
import com.lizl.mydiary.adapter.MoodListAdapter
import com.lizl.mydiary.util.AppConstant
import kotlinx.android.synthetic.main.dialog_mood_select.*

class DialogMoodSelect(context: Context, private val onMoodSelectListener: (mood: Int) -> Unit) : BaseDialog(context)
{
    override fun getDialogContentViewResId() = R.layout.dialog_mood_select

    override fun initView()
    {
        rv_mood_grid.layoutManager = GridLayoutManager(context, 3)
        val moodListAdapter = MoodListAdapter()
        rv_mood_grid.adapter = moodListAdapter

        val moodList = listOf(R.mipmap.ic_mood_happy, R.mipmap.ic_mood_normal, R.mipmap.ic_mood_unhappy)
        moodListAdapter.setData(moodList)

        moodListAdapter.setOnMoodItemClickListener {
            when (it)
            {
                R.mipmap.ic_mood_happy   -> onMoodSelectListener.invoke(AppConstant.MOOD_HAPPY)
                R.mipmap.ic_mood_normal  -> onMoodSelectListener.invoke(AppConstant.MOOD_NORMAL)
                R.mipmap.ic_mood_unhappy -> onMoodSelectListener.invoke(AppConstant.MOOD_UNHAPPY)
            }
            dismiss()
        }
    }

    override fun getDialogWidth() = context.resources.getDimensionPixelOffset(R.dimen.dialog_mood_select_width)
}