package com.lizl.mydiary.mvp.activity

import android.content.Intent
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.lizl.mydiary.R
import com.lizl.mydiary.UiApplication
import com.lizl.mydiary.adapter.DiaryListAdapter
import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.bean.OperationItem
import com.lizl.mydiary.bean.TitleBarBtnBean
import com.lizl.mydiary.config.ConfigConstant
import com.lizl.mydiary.mvp.base.BaseActivity
import com.lizl.mydiary.mvp.contract.DiaryListContract
import com.lizl.mydiary.mvp.presenter.DiaryListPresenter
import com.lizl.mydiary.util.AppConstant
import com.lizl.mydiary.util.BackupUtil
import com.lizl.mydiary.util.DialogUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_diary_list_herder.*

class MainActivity : BaseActivity<DiaryListPresenter>(), DiaryListContract.View
{

    private lateinit var diaryListAdapter: DiaryListAdapter

    override fun getLayoutResId() = R.layout.activity_main

    override fun initPresenter() = DiaryListPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        // Activity走onCreate()将上次应用停止时间置为0，保证onResume()会走是否显示锁定界面流程
        UiApplication.appConfig.setAppLastStopTime(0)

        if (UiApplication.appConfig.isAutoBackup() && System.currentTimeMillis() - UiApplication.appConfig.getLastAutoBackupTime() > ConfigConstant.APP_AUTO_BACKUP_PERIOD)
        {
            BackupUtil.autoBackup()
        }
    }

    override fun initView()
    {
        diaryListAdapter = DiaryListAdapter()
        rv_diary_list.layoutManager = LinearLayoutManager(this)
        rv_diary_list.adapter = diaryListAdapter

        val titleBtnList = mutableListOf<TitleBarBtnBean.BaseBtnBean>()
        titleBtnList.add(TitleBarBtnBean.ImageBtnBean(R.mipmap.ic_setting) { turnToSettingActivity() })
        titleBtnList.add(TitleBarBtnBean.ImageBtnBean(R.mipmap.ic_search) {
            ctb_title.startSearchMode({ presenter.searchDiary(it) }, { presenter.queryAllDiary() })
        })
        ctb_title.setTitleText(getString(R.string.app_name))
        ctb_title.setBtnList(titleBtnList)

        fab_add_diary.setOnClickListener { turnToDiaryContentActivity(null) }

        diaryListAdapter.setOnDiaryItemClickListener { turnToDiaryContentActivity(it) }

        diaryListAdapter.setOnDiaryItemLongClickListener { showDiaryOperationListDialog(it) }

        presenter.queryAllDiary()
    }

    private fun turnToDiaryContentActivity(diaryBean: DiaryBean?)
    {
        val intent = Intent(this, DiaryContentActivity::class.java)
        intent.putExtra(AppConstant.BUNDLE_DATA_OBJECT, diaryBean)
        startActivity(intent)
    }

    private fun showDiaryOperationListDialog(diaryBean: DiaryBean)
    {
        val operationList = mutableListOf<OperationItem>()
        operationList.add(OperationItem(getString(R.string.delete)) { presenter.deleteDiary(diaryBean) })
        DialogUtil.showOperationListDialog(this, operationList)
    }

    override fun onDiariesQueryFinish(diaryList: List<DiaryBean>)
    {
        diaryListAdapter.clear()
        diaryListAdapter.addAll(diaryList)
        updateDiaryListHeader()
    }

    override fun onDiaryDeleted(diaryBean: DiaryBean)
    {
        diaryListAdapter.remove(diaryBean)
        updateDiaryListHeader()
    }

    override fun onDiaryInsert(diaryBean: DiaryBean)
    {
        diaryListAdapter.insertDiary(diaryBean)
        updateDiaryListHeader()
    }

    override fun showDiarySearchResult(diaryList: List<DiaryBean>)
    {
        diaryListAdapter.clear()
        diaryListAdapter.addAll(diaryList)
        updateDiaryListHeader()
    }

    private fun updateDiaryListHeader()
    {
        tv_end_footer.isVisible = diaryListAdapter.getData().isNotEmpty()
        layout_diary_header.isVisible = diaryListAdapter.getData().isNotEmpty()
        tv_header_content.text = getString(R.string.diary_total_count, diaryListAdapter.getData().size)
    }

    override fun onBackPressed()
    {
        if (ctb_title.inSearchMode)
        {
            ctb_title.stopSearchMode()
            return
        }
        super.onBackPressed()
    }
}
