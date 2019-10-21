package com.lizl.mydiary.mvp.contract

import com.lizl.mydiary.mvp.base.BasePresenter
import com.lizl.mydiary.mvp.base.BaseView
import java.io.File

class BackupFileListFragmentContract
{
    interface View : BaseView
    {
        fun showBackupFileList(fileList: List<File>)

        fun onBackupFileDeleted(file: File)

        fun showRestoringDataView()

        fun onRestoreDataFinish(result: Boolean)
    }

    interface Presenter : BasePresenter<View>
    {
        fun getBackupFileList()

        fun deleteBackupFile(file: File)

        fun restoreData(file: File)
    }
}