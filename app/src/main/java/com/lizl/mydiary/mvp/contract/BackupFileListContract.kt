package com.lizl.mydiary.mvp.contract

import com.lizl.mydiary.mvp.base.BasePresenter
import com.lizl.mydiary.mvp.base.BaseView
import java.io.File

class BackupFileListContract
{
    interface View : BaseView
    {
        fun showFileFindingView()

        fun showBackupFileList(fileList: List<File>)

        fun onBackupFileDeleted(file: File)

        fun onBackupFileUpdate(file: File)

        fun showRestoringDataView()

        fun onRestoreDataFinish(result: Boolean, backupFile: File, failedReason: String)
    }

    interface Presenter : BasePresenter<View>
    {
        fun getBackupFileList()

        fun deleteBackupFile(file: File)

        fun restoreData(file: File)

        fun restoreData(file: File, password: String)

        fun renameBackupFile(file: File, newName: String)

        fun clearBackupFiles()
    }
}