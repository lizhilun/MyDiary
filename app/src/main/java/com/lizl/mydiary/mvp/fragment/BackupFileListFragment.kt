package com.lizl.mydiary.mvp.fragment

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.lizl.mydiary.R
import com.lizl.mydiary.adapter.BackupFileListAdapter
import com.lizl.mydiary.bean.OperationItem
import com.lizl.mydiary.mvp.base.BaseFragment
import com.lizl.mydiary.mvp.presenter.EmptyPresenter
import com.lizl.mydiary.util.BackupUtil
import com.lizl.mydiary.util.DialogUtil
import kotlinx.android.synthetic.main.fragment_backup_file_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class BackupFileListFragment : BaseFragment<EmptyPresenter>(), BackupFileListAdapter.OnBackFileItemClickListener
{

    override fun getLayoutResId() = R.layout.fragment_backup_file_list

    override fun initPresenter() = EmptyPresenter()

    override fun initTitleBar()
    {
        ctb_title.setOnBackBtnClickListener { backToPreFragment() }
    }

    override fun initView()
    {

    }

    override fun onResume()
    {
        super.onResume()

        //TODO:延时300ms加载数据，防止界面切换卡顿
        GlobalScope.launch(Dispatchers.Main) {
            delay(300)
            getData()
        }
    }

    private fun getData()
    {
        val backupFileList = BackupUtil.getBackupFileList()
        val backupFileListAdapter = BackupFileListAdapter(backupFileList, this)
        rv_file_list.layoutManager = LinearLayoutManager(activity)
        rv_file_list.adapter = backupFileListAdapter
    }

    override fun onBackupFileItemClick(file: File)
    {
        val operationList = mutableListOf<OperationItem>()

        operationList.add(OperationItem(getString(R.string.import_backup_file)) {
            DialogUtil.showLoadingDialog(context!!, getString(R.string.in_restore_data))
            BackupUtil.restoreData(file.absolutePath) {
                DialogUtil.dismissDialog()
                ToastUtils.showShort(if (it) R.string.success_to_restore_data else R.string.failed_to_restore_data)
            }
        })

        DialogUtil.showOperationListDialog(activity as Context, operationList)
    }

    override fun onBackPressed() = false
}