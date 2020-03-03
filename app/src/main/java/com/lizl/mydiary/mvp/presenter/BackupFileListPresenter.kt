package com.lizl.mydiary.mvp.presenter

import com.lizl.mydiary.event.UIEvent
import com.lizl.mydiary.mvp.contract.BackupFileListContract
import com.lizl.mydiary.util.BackupUtil
import com.lizl.mydiary.util.FileUtil
import kotlinx.coroutines.*
import java.io.File

class BackupFileListPresenter(private var view: BackupFileListContract.View?) :
    BackupFileListContract.Presenter {

    override fun getBackupFileList() {
        GlobalScope.launch {
            val job = GlobalScope.launch(Dispatchers.Main) {
                delay(200)
                view?.showFileFindingView()
            }
            val backupFileList = BackupUtil.getBackupFileList()
            job.cancel()
            GlobalScope.launch(Dispatchers.Main) { view?.showBackupFileList(backupFileList) }
        }
    }

    override fun deleteBackupFile(file: File) {
        GlobalScope.launch {
            val result = FileUtil.deleteFile(file.absolutePath)
            GlobalScope.launch(Dispatchers.Main) { if (result) view?.onBackupFileDeleted(file) }
        }
    }

    override fun restoreData(file: File) {
        GlobalScope.launch {
            GlobalScope.launch(Dispatchers.Main) { view?.showRestoringDataView() }
            BackupUtil.restoreData(file.absolutePath) { result, failedReason ->
                GlobalScope.launch(Dispatchers.Main) {
                    view?.onRestoreDataFinish(
                        result,
                        file,
                        failedReason
                    )
                }
            }
        }
    }

    override fun restoreData(file: File, password: String) {
        GlobalScope.launch {
            GlobalScope.launch(Dispatchers.Main) { view?.showRestoringDataView() }
            BackupUtil.restoreData(file.absolutePath, password) { result, failedReason ->
                GlobalScope.launch(Dispatchers.Main) {
                    view?.onRestoreDataFinish(
                        result,
                        file,
                        failedReason
                    )
                }
            }
        }
    }

    override fun renameBackupFile(file: File, newName: String) {
        GlobalScope.launch {
            FileUtil.renameFile(file, newName, true)
            delay(500)
            getBackupFileList()
        }
    }

    override fun clearBackupFiles() {
        GlobalScope.launch {
            val backupFileList = BackupUtil.getBackupFileList()
            val latestFile = backupFileList.maxBy { it.lastModified() } ?: return@launch
            backupFileList.forEach { if (latestFile != it) FileUtil.deleteFile(it) }
            GlobalScope.launch(Dispatchers.Main) { view?.showBackupFileList(listOf(latestFile)) }
        }
    }

    override fun handleUIEvent(uiEvent: UIEvent) {

    }

    override fun onDestroy() {
        view = null
    }
}