package com.lizl.mydiary.custom.dialog

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.lizl.mydiary.R
import com.lizl.mydiary.adapter.MoodListAdapter
import com.lizl.mydiary.util.DiaryUtil
import kotlinx.android.synthetic.main.dialog_mood_select.*

class DialogMoodSelect(context: Context, private val withAll: Boolean, private val onMoodSelectListener: (mood: Int) -> Unit) : BaseDialog(context, null)
{
    override fun getDialogContentView(): View = layoutInflater.inflate(R.layout.dialog_mood_select, null)

    override fun initView()
    {
        val moodListAdapter = MoodListAdapter()
        rv_mood_grid.layoutManager = GridLayoutManager(context, DiaryUtil.getMoodList(withAll).size)
        rv_mood_grid.adapter = moodListAdapter

        moodListAdapter.setData(DiaryUtil.getMoodResList(withAll))

        moodListAdapter.setOnMoodItemClickListener {
            onMoodSelectListener.invoke(DiaryUtil.getMoodByMoodRes(it))
            dismiss()
        }
    }

    override fun getDialogWidth() = context.resources.getDimensionPixelOffset(R.dimen.dialog_mood_select_width)

    override fun onConfirmBtnClick() = true
}