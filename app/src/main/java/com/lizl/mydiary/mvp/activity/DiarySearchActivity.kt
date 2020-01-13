package com.lizl.mydiary.mvp.activity

import com.lizl.mydiary.R
import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.bean.TitleBarBtnBean
import com.lizl.mydiary.mvp.base.BaseActivity
import com.lizl.mydiary.mvp.contract.DiarySearchContract
import com.lizl.mydiary.mvp.presenter.DiarySearchPresenter
import com.lizl.mydiary.util.AppConstant
import com.lizl.mydiary.util.DialogUtil
import com.lizl.mydiary.util.DiaryUtil
import kotlinx.android.synthetic.main.activity_diary_search.*

class DiarySearchActivity : BaseActivity<DiarySearchPresenter>(), DiarySearchContract.View
{
    override fun getLayoutResId() = R.layout.activity_diary_search

    override fun initPresenter() = DiarySearchPresenter(this)

    private var curDiaryMood = AppConstant.MOOD_ALL

    override fun initView()
    {
        val keyWord = intent?.getStringExtra(AppConstant.BUNDLE_DATA_STRING) ?: ""
        val mood = intent?.getIntExtra(AppConstant.BUNDLE_DATA_INT, -1) ?: -1

        ctb_title.startSearchMode(keyWord, true) { presenter.searchDiary(it, curDiaryMood) }
        ctb_title.setOnBackBtnClickListener { super.onBackPressed() }

        showDiaryMood(if (mood == -1) AppConstant.MOOD_ALL else mood)

        presenter.searchDiary(keyWord, mood)
    }

    private fun showDiaryMood(mood: Int)
    {
        curDiaryMood = mood
        ctb_title.setBtnList(mutableListOf<TitleBarBtnBean.BaseBtnBean>().apply {
            add(TitleBarBtnBean.ImageBtnBean(DiaryUtil.getMoodResByMood(mood)) {
                DialogUtil.showMoodSelectDialog(this@DiarySearchActivity, true) {
                    showDiaryMood(it)
                    presenter.searchDiary(ctb_title.getSearchText(), it)
                }
            })
        })
    }

    override fun showDiaryResult(diaryList: List<DiaryBean>)
    {
        view_diary_list.showDiaryList(diaryList)
    }
}