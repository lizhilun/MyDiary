package com.lizl.mydiary.mvp.activity

import android.os.Bundle
import com.lizl.mydiary.R
import com.lizl.mydiary.UiApplication
import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.bean.TitleBarBtnBean
import com.lizl.mydiary.config.ConfigConstant
import com.lizl.mydiary.mvp.base.BaseActivity
import com.lizl.mydiary.mvp.contract.DiaryListContract
import com.lizl.mydiary.mvp.presenter.DiaryListPresenter
import com.lizl.mydiary.util.ActivityUtil
import com.lizl.mydiary.util.BackupUtil
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity<DiaryListPresenter>(), DiaryListContract.View
{

    override fun getLayoutResId() = R.layout.activity_main

    override fun initPresenter() = DiaryListPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        if (UiApplication.appConfig.isAutoBackup() && System.currentTimeMillis() - UiApplication.appConfig.getLastAutoBackupTime() > ConfigConstant.APP_AUTO_BACKUP_PERIOD)
        {
            BackupUtil.autoBackup()
        }
    }

    override fun initView()
    {
        val titleBtnList = mutableListOf<TitleBarBtnBean.BaseBtnBean>()
        titleBtnList.add(TitleBarBtnBean.ImageBtnBean(R.drawable.ic_setting) { ActivityUtil.turnToActivity(SettingActivity::class.java) })
        titleBtnList.add(TitleBarBtnBean.ImageBtnBean(R.drawable.ic_search) { ActivityUtil.turnToActivity(DiarySearchActivity::class.java, -1) })
        ctb_title.setBtnList(titleBtnList)

        fab_add_diary.setOnClickListener { ActivityUtil.turnToActivity(DiaryContentActivity::class.java) }

        presenter.queryAllDiary()
    }

    override fun onDiariesQueryFinish(diaryList: List<DiaryBean>)
    {
        view_diary_list.showDiaryList(diaryList)
    }

    override fun onDiarySaveSuccess(diaryBean: DiaryBean)
    {
        view_diary_list.onDiarySaveSuccess(diaryBean)
    }
}
