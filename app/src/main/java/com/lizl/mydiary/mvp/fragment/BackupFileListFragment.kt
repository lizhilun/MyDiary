package com.lizl.mydiary.mvp.fragment

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.lizl.mydiary.R
import com.lizl.mydiary.adapter.BackupFileListAdapter
import com.lizl.mydiary.bean.OperationItem
import com.lizl.mydiary.mvp.base.BaseFragment
import com.lizl.mydiary.mvp.contract.BackupFileListFragmentContract
import com.lizl.mydiary.mvp.presenter.BackupFileListFragmentPresenter
import com.lizl.mydiary.util.DialogUtil
import kotlinx.android.synthetic.main.fragment_backup_file_list.*
import java.io.File

class BackupFileListFragment : BaseFragment<BackupFileListFragmentPresenter>(), BackupFileListFragmentContract.View
{

    override fun getLayoutResId() = R.layout.fragment_backup_file_list

    private lateinit var backupFileListAdapter: BackupFileListAdapter

    override fun initPresenter() = BackupFileListFragmentPresenter(this)

    override fun initTitleBar()
    {
        ctb_title.setOnBackBtnClickListener { backToPreFragment() }
    }

    override fun initView()
    {
        backupFileListAdapter = BackupFileListAdapter()
        rv_file_list.layoutManager = LinearLayoutManager(activity)
        rv_file_list.adapter = backupFileListAdapter

        presenter.getBackupFileList()

        backupFileListAdapter.setOnFileItemClickListener { showFileOperationDialog(it) }
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
        DialogUtil.showLoadingDialog(context!!, getString(R.string.in_restore_data))
    }

    override fun onRestoreDataFinish(result: Boolean)
    {
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