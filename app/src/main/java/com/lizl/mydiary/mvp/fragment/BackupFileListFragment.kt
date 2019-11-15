package com.lizl.mydiary.mvp.fragment

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.lizl.mydiary.R
import com.lizl.mydiary.UiApplication
import com.lizl.mydiary.adapter.BackupFileListAdapter
import com.lizl.mydiary.bean.OperationItem
import com.lizl.mydiary.event.EventConstant
import com.lizl.mydiary.event.UIEvent
import com.lizl.mydiary.mvp.base.BaseFragment
import com.lizl.mydiary.mvp.contract.BackupFileListContract
import com.lizl.mydiary.mvp.presenter.BackupFileListPresenter
import com.lizl.mydiary.util.DialogUtil
import kotlinx.android.synthetic.main.fragment_backup_file_list.*
import org.greenrobot.eventbus.EventBus
import java.io.File

class BackupFileListFragment : BaseFragment<BackupFileListPresenter>(), BackupFileListContract.View
{

    override fun getLayoutResId() = R.layout.fragment_backup_file_list

    private lateinit var backupFileListAdapter: BackupFileListAdapter

    override fun initPresenter() = BackupFileListPresenter(this)

    override fun initView()
    {
        backupFileListAdapter = BackupFileListAdapter()
        rv_file_list.layoutManager = LinearLayoutManager(activity)
        rv_file_list.adapter = backupFileListAdapter

        backupFileListAdapter.setOnFileItemClickListener { showFileOperationDialog(it) }

        ctb_title.setOnBackBtnClickListener { backToPreFragment() }

        presenter.getBackupFileList()
    }

    override fun showBackupFileList(fileList: List<File>)
    {
        backupFileListAdapter.addAll(fileList)
    }

    override fun onBackupFileDeleted(file: File)
    {
        backupFileListAdapter.remove(file)
    }

    override fun showRestoringDataView()
    {
        DialogUtil.showLoadingDialog(context!!, getString(R.string.in_doing, getString(R.string.restore_data)))
    }

    override fun onRestoreDataFinish(result: Boolean)
    {
        if (result)
        {
            EventBus.getDefault().post(UIEvent(EventConstant.UI_EVENT_IMPORT_DIARY_DATA))
        }
        ToastUtils.showShort(UiApplication.instance.getString(R.string.restore_data) + UiApplication.instance.getString(
                if (result) R.string.success else R.string.failed))
        DialogUtil.dismissDialog()
    }

    private fun showFileOperationDialog(file: File)
    {
        val operationList = mutableListOf<OperationItem>()

        operationList.add(OperationItem(getString(R.string.import_backup_file)) { presenter.restoreData(file) })

        operationList.add(OperationItem(getString(R.string.delete_backup_file)) { presenter.deleteBackupFile(file) })

        DialogUtil.showOperationListDialog(activity as Context, operationList)
    }
}