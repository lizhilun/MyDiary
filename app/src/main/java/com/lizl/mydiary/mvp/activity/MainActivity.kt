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
import com.lizl.mydiary.event.DiarySaveEvent
import com.lizl.mydiary.mvp.base.BaseActivity
import com.lizl.mydiary.mvp.contract.DiaryListFragmentContract
import com.lizl.mydiary.mvp.presenter.DiaryListFragmentPresenter
import com.lizl.mydiary.util.AppConstant
import com.lizl.mydiary.util.DialogUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_diary_list_herder.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : BaseActivity<DiaryListFragmentPresenter>(), DiaryListFragmentContract.View
{

    private lateinit var diaryListAdapter: DiaryListAdapter

    override fun getLayoutResId() = R.layout.activity_main

    override fun initPresenter() = DiaryListFragmentPresenter(this)

    override fun needRegisterEvent() = true

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        // Activity走onCreate()将上次应用停止时间置为0，保证onResume()会走是否显示锁定界面流程
        UiApplication.appConfig.setAppLastStopTime(0)
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

        fab_add_diary.setOnClickListener { turnToDiaryContentFragment(null) }

        diaryListAdapter.setOnDiaryItemClickListener { turnToDiaryContentFragment(it) }

        diaryListAdapter.setOnDiaryItemLongClickListener { showDiaryOperationListDialog(it) }

        presenter.queryAllDiary()
    }

    private fun turnToDiaryContentFragment(diaryBean: DiaryBean?)
    {
        val intent = Intent(this, DiaryContentActivity::class.java)
        if (diaryBean != null)
        {
            intent.putExtra(AppConstant.BUNDLE_DATA_OBJECT, diaryBean)
        }
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onDiarySave(diarySaveEvent: DiarySaveEvent)
    {
        val findDiaryBean = diaryListAdapter.getData().find { it.id == diarySaveEvent.diaryBean.id }
        if (findDiaryBean != null)
        {
            diaryListAdapter.remove(findDiaryBean)
        }
        diaryListAdapter.insertDiary(diarySaveEvent.diaryBean)
    }
}
