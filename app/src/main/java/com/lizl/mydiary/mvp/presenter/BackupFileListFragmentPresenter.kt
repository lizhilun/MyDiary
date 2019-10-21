package com.lizl.mydiary.mvp.presenter

import com.lizl.mydiary.mvp.contract.BackupFileListFragmentContract
import com.lizl.mydiary.util.BackupUtil
import com.lizl.mydiary.util.FileUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class BackupFileListFragmentPresenter(private var view: BackupFileListFragmentContract.View?) : BackupFileListFragmentContract.Presenter
{
    override fun getBackupFileList()
    {
        GlobalScope.launch {
            val backupFileList = BackupUtil.getBackupFileList()
            GlobalScope.launch(Dispatchers.Main) {
                view?.showBackupFileList(backupFileList)
            }
        }
    }

    override fun deleteBackupFile(file: File)
    {
        GlobalScope.launch {
            val result = FileUtil.deleteFile(file.absolutePath)
            GlobalScope.launch(Dispatchers.Main) {
                if (result) view?.onBackupFileDeleted(file)
            }
        }
    }

    override fun restoreData(file: File)
    {
        GlobalScope.launch {
            BackupUtil.restoreData(file.absolutePath) {
                GlobalScope.launch(Dispatchers.Main) {
                    view?.onRestoreDataFinish(it)
                }
            }
        }
    }

    override fun onDestroy()
    {
        view = null
    }
}