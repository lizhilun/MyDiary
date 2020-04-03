package com.lizl.mydiary.mvp.activity

import com.lizl.mydiary.R
import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.bean.TitleBarBtnBean
import com.lizl.mydiary.constant.AppConstant
import com.lizl.mydiary.mvp.base.BaseActivity
import com.lizl.mydiary.mvp.contract.DiarySearchContract
import com.lizl.mydiary.mvp.presenter.DiarySearchPresenter
import com.lizl.mydiary.util.DiaryUtil
import com.lizl.mydiary.util.PopupUtil
import kotlinx.android.synthetic.main.activity_diary_search.*

class DiarySearchActivity : BaseActivity<DiarySearchPresenter>(), DiarySearchContract.View
{

    private lateinit var moodTitleBtn: TitleBarBtnBean.ImageBtnBean

    override fun getLayoutResId() = R.layout.activity_diary_search

    override fun initPresenter() = DiarySearchPresenter(this)

    override fun initView()
    {
        val keyWord = intent?.getStringExtra(AppConstant.BUNDLE_DATA_STRING).orEmpty()
        val mood = intent?.getIntExtra(AppConstant.BUNDLE_DATA_INT, AppConstant.MOOD_ALL) ?: AppConstant.MOOD_ALL

        ctb_title.startSearchMode(keyWord, true) { presenter.searchDiary(it, mood) }
        ctb_title.setOnBackBtnClickListener { super.onBackPressed() }

        moodTitleBtn = TitleBarBtnBean.ImageBtnBean(DiaryUtil.getMoodResByMood(mood)) {
            PopupUtil.showMoodSelectPopup(true) {
                moodTitleBtn.imageRedId = DiaryUtil.getMoodResByMood(it)
                ctb_title.updateBtn(moodTitleBtn)
                presenter.searchDiary(ctb_title.getSearchText(), it)
            }
        }

        ctb_title.setBtnList(mutableListOf<TitleBarBtnBean.BaseBtnBean>().apply {
            add(moodTitleBtn)
        })

        presenter.searchDiary(keyWord, mood)
    }

    override fun showDiaryResult(diaryList: List<DiaryBean>)
    {
        view_diary_list.showDiaryList(diaryList)
    }
}