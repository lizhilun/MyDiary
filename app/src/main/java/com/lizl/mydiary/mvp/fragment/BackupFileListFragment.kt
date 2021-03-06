package com.lizl.mydiary.mvp.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.animation.SlideInRightAnimation
import com.lizl.mydiary.R
import com.lizl.mydiary.adapter.BackupFileListAdapter
import com.lizl.mydiary.bean.OperationItem
import com.lizl.mydiary.bean.TitleBarBtnBean
import com.lizl.mydiary.constant.AppConstant
import com.lizl.mydiary.custom.others.CustomDiffUtil
import com.lizl.mydiary.mvp.base.BaseFragment
import com.lizl.mydiary.mvp.contract.BackupFileListContract
import com.lizl.mydiary.mvp.presenter.BackupFileListPresenter
import com.lizl.mydiary.util.FileUtil
import com.lizl.mydiary.util.PopupUtil
import kotlinx.android.synthetic.main.fragment_backup_file_list.*
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

        val callback = CustomDiffUtil<File>({ oldItem, newItem -> oldItem.absolutePath == newItem.absolutePath },
                { oldItem, newItem -> oldItem.absolutePath == newItem.absolutePath })

        backupFileListAdapter.adapterAnimation = SlideInRightAnimation()
        backupFileListAdapter.setDiffCallback(callback)

        backupFileListAdapter.setOnFileItemClickListener { showFileOperationDialog(it) }

        val titleBtnList = mutableListOf<TitleBarBtnBean.BaseBtnBean>()
        titleBtnList.add(TitleBarBtnBean.ImageBtnBean(R.drawable.ic_clear) {
            PopupUtil.showOperationConfirmPopup(getString(R.string.clear_backup_data), getString(R.string.notify_clear_backup_data)) {
                presenter.clearBackupFiles()
            }
        })
        ctb_title.setBtnList(titleBtnList)

        ctb_title.setOnBackBtnClickListener { backToPreFragment() }

        presenter.getBackupFileList()
    }

    override fun showFileFindingView()
    {
        PopupUtil.showLoadingPopup(getString(R.string.in_finding_backup_file))
    }

    override fun showBackupFileList(fileList: List<File>)
    {
        PopupUtil.dismissAll()

        backupFileListAdapter.setDiffNewData(fileList.toMutableList())
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
        PopupUtil.showLoadingPopup(getString(R.string.in_doing, getString(R.string.restore_data)))
    }

    override fun onRestoreDataFinish(result: Boolean, backupFile: File, failedReason: String)
    {
        when (failedReason)
        {
            AppConstant.RESTORE_DATA_FAILED_WRONG_PASSWORD ->
            {
                PopupUtil.showOperationConfirmPopup("${getString(R.string.restore_data)}${getString(R.string.failed)}",
                        getString(R.string.notify_restore_data_failed_wrong_password)) {
                    PopupUtil.showInputPasswordPopup() {
                        presenter.restoreData(backupFile, it)
                    }
                }
                return
            }
        }
        ToastUtils.showShort("${getString(R.string.restore_data)}${getString(if (result) R.string.success else R.string.failed)}")
        PopupUtil.dismissAll()
    }

    private fun showFileOperationDialog(file: File)
    {
        val operationList = mutableListOf<OperationItem>().apply {

            add(OperationItem(getString(R.string.import_backup_file)) { presenter.restoreData(file) })

            add(OperationItem(getString(R.string.delete_backup_file)) { presenter.deleteBackupFile(file) })

            add(OperationItem(getString(R.string.rename_backup_file)) {
                PopupUtil.showInputPopup(getString(R.string.rename_backup_file), file.nameWithoutExtension, getString(R.string.hint_rename_backup_file)) {
                    presenter.renameBackupFile(file, it)
                }
            })

            add(OperationItem(getString(R.string.share_backup_file)) { FileUtil.shareAllTypeFile(file) })
        }

        PopupUtil.showOperationListPopup(operationList)
    }
}