package com.lizl.mydiary.mvp.fragment

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.lizl.mydiary.R
import com.lizl.mydiary.adapter.BackupFileListAdapter
import com.lizl.mydiary.bean.OperationItem
import com.lizl.mydiary.bean.TitleBarBtnBean
import com.lizl.mydiary.event.EventConstant
import com.lizl.mydiary.event.UIEvent
import com.lizl.mydiary.mvp.base.BaseFragment
import com.lizl.mydiary.mvp.contract.BackupFileListContract
import com.lizl.mydiary.mvp.presenter.BackupFileListPresenter
import com.lizl.mydiary.util.AppConstant
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

        val titleBtnList = mutableListOf<TitleBarBtnBean.BaseBtnBean>()
        titleBtnList.add(TitleBarBtnBean.ImageBtnBean(R.drawable.ic_clear) {
            DialogUtil.showOperationConfirmDialog(activity as Context, getString(R.string.clear_backup_data), getString(R.string.notify_clear_backup_data)) {
                presenter.clearBackupFiles()
            }
        })
        ctb_title.setBtnList(titleBtnList)

        ctb_title.setOnBackBtnClickListener { backToPreFragment() }

        presenter.getBackupFileList()
    }

    override fun showBackupFileList(fileList: List<File>)
    {
        backupFileListAdapter.clear()
        backupFileListAdapter.addAll(fileList)
    }

    override fun onBackupFileDeleted(file: File)
    {
        backupFileListAdapter.remove(file)
    }

    override fun onBackupFileUpdate(file: File)
    {
        backupFileListAdapter.update(file)
    }

    override fun showRestoringDataView()
    {
        DialogUtil.showLoadingDialog(activity as Context, getString(R.string.in_doing, getString(R.string.restore_data)))
    }

    override fun onRestoreDataFinish(result: Boolean, backupFile: File, failedReason: String)
    {
        if (result)
        {
            EventBus.getDefault().post(UIEvent(EventConstant.UI_EVENT_IMPORT_DIARY_DATA))
        }
        else
        {
            when (failedReason)
            {
                AppConstant.RESTORE_DATA_FAILED_WRONG_PASSWORD ->
                {
                    DialogUtil.showOperationConfirmDialog(activity as Context, "${getString(R.string.restore_data)}${getString(R.string.failed)}",
                            getString(R.string.notify_restore_data_failed_wrong_password)) {
                        DialogUtil.showInputPasswordDialog(activity as Context) {
                            presenter.restoreData(backupFile, it)
                        }
                    }
                    return
                }
            }
        }
        ToastUtils.showShort("${getString(R.string.restore_data)}${getString(if (result) R.string.success else R.string.failed)}")
        DialogUtil.dismissDialog()
    }

    private fun showFileOperationDialog(file: File)
    {
        val operationList = mutableListOf<OperationItem>().apply {

            add(OperationItem(getString(R.string.import_backup_file)) { presenter.restoreData(file) })

            add(OperationItem(getString(R.string.delete_backup_file)) { presenter.deleteBackupFile(file) })

            add(OperationItem(getString(R.string.rename_backup_file)) {
                DialogUtil.showInputDialog(activity as Context, getString(R.string.rename_backup_file), file.nameWithoutExtension,
                        getString(R.string.hint_rename_backup_file)) {
                    presenter.renameBackupFile(file, it)
                }
            })
        }

        DialogUtil.showOperationListDialog(activity as Context, operationList)
    }
}