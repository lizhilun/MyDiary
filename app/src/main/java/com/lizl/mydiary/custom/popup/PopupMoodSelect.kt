package com.lizl.mydiary.custom.popup

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import com.lizl.mydiary.R
import com.lizl.mydiary.adapter.MoodListAdapter
import com.lizl.mydiary.util.DiaryUtil
import com.lxj.xpopup.core.CenterPopupView
import kotlinx.android.synthetic.main.popup_mood_select.view.*

class PopupMoodSelect(context: Context, private val withAll: Boolean, private val onMoodSelectListener: (mood: Int) -> Unit) : CenterPopupView(context)
{

    override fun getImplLayoutId() = R.layout.popup_mood_select

    override fun onCreate()
    {
        val moodListAdapter = MoodListAdapter()
        rv_mood_grid.layoutManager = GridLayoutManager(context, DiaryUtil.getMoodList(withAll).size)
        rv_mood_grid.adapter = moodListAdapter

        moodListAdapter.setNewData(DiaryUtil.getMoodResList(withAll).toMutableList())

        moodListAdapter.setOnMoodItemClickListener {
            onMoodSelectListener.invoke(DiaryUtil.getMoodByMoodRes(it))
            dismiss()
        }
    }
}