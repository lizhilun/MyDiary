package com.lizl.mydiary.util

import android.provider.MediaStore
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.ToastUtils
import com.lizl.mydiary.R
import com.lizl.mydiary.UiApplication
import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.config.AppConfig
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object BackupUtil
{
    private val backupFilePath = PathUtils.getExternalStoragePath() + "/DiaryBackup"
    private val backupTempFilePath = "$backupFilePath/temp"
    private val backupTempImageFilePath = "$backupFilePath/temp/picture"
    private val backupTempDiaryFilePath = "$backupFilePath/temp/diary.txt"
    private const val backupFileSuffix = ".iui"
    private const val autoBackupFileName = "autoBackup"

    private val channel = Channel<BackupJob>()

    init
    {
        GlobalScope.launch {
            while (true)
            {
                val job = channel.receive()
                backupData(job.backupFileName, job.callback)
            }
        }
    }

    fun manualBackupData(callback: (result: Boolean) -> Unit)
    {
        val formatter = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val backupFileName = formatter.format(System.currentTimeMillis())

        GlobalScope.launch {
            channel.send(BackupJob(backupFileName, callback))
        }
    }

    fun autoBackup()
    {
        GlobalScope.launch {
            channel.send(BackupJob(autoBackupFileName) {
                if (it)
                {
                    AppConfig.getBackupConfig().setLastAutoBackupTime(System.currentTimeMillis())
                }
                ToastUtils.showShort(UiApplication.instance.getString(R.string.auto_backup_data) + UiApplication.instance.getString(
                        if (it) R.string.success else R.string.failed))
            })
        }
    }

    private suspend fun backupData(backupFileName: String, callback: (result: Boolean) -> Unit)
    {
        try
        {
            val diaryList = AppDatabase.instance.getDiaryDao().getAllDiary()
            val diaryListText = GsonUtils.toJson(diaryList)
            if (!FileUtil.writeTxtFile(diaryListText, backupTempDiaryFilePath))
            {
                FileUtils.deleteDir(backupTempFilePath)
                callback.invoke(false)
                return
            }

            if (!FileUtil.copyDir(FileUtil.getImageFileSavePath(), backupTempImageFilePath))
            {
                FileUtils.deleteDir(backupTempFilePath)
                callback.invoke(false)
                return
            }

            val zipFilePath = "$backupFilePath/$backupFileName$backupFileSuffix"
            FileUtil.deleteFile(zipFilePath)
            val zipResult = ZipUtil.zipFile(backupTempFilePath, zipFilePath,
                    if (AppConfig.getBackupConfig().isBackupFileEncryption()) AppConfig.getSecurityConfig().getAppLockPassword() else "")

            FileUtils.deleteDir(backupTempFilePath)

            FileUtils.notifySystemToScan(backupFilePath)

            delay(1000)

            callback.invoke(zipResult)
        }
        catch (e: Exception)
        {
            FileUtils.deleteDir(backupTempFilePath)
            callback.invoke(false)
        }
    }

    fun restoreData(restoreFilePath: String, callback: (result: Boolean, failedReason: String) -> Unit)
    {
        restoreData(restoreFilePath, AppConfig.getSecurityConfig().getAppLockPassword(), callback)
    }

    fun restoreData(restoreFilePath: String, password: String, callback: (result: Boolean, failedReason: String) -> Unit)
    {
        GlobalScope.launch {

            try
            {
                val unzipResult = ZipUtil.unZipFile(restoreFilePath, backupFilePath, password)

                if (unzipResult != ZipUtil.UNZIP_SUCCESS)
                {
                    FileUtil.deleteFile(backupTempFilePath)
                    when (unzipResult)
                    {
                        ZipUtil.UNZIP_FAILED_WRONG_PASSWORD -> callback.invoke(false, AppConstant.RESTORE_DATA_FAILED_WRONG_PASSWORD)
                        else                                -> callback.invoke(false, AppConstant.RESTORE_DATA_FAILED_WRONG_BACKUP_FILE)
                    }
                    return@launch
                }

                val diaryFile = File(backupTempDiaryFilePath)

                if (!diaryFile.exists())
                {
                    FileUtil.deleteFile(backupTempFilePath)
                    callback.invoke(false, AppConstant.RESTORE_DATA_FAILED_WRONG_BACKUP_FILE)
                    return@launch
                }

                val diaryTxt = FileUtil.readTxtFile(backupTempDiaryFilePath)

                FileUtil.copyDir(backupTempImageFilePath, FileUtil.getImageFileSavePath())
                val diaryList = GsonUtils.fromJson<Array<DiaryBean>>(diaryTxt, Array<DiaryBean>::class.java)
                val saveDiaryList = diaryList.filter { AppDatabase.instance.getDiaryDao().getDiaryByUid(it.uid) == null }
                AppDatabase.instance.getDiaryDao().insertList(saveDiaryList as MutableList<DiaryBean>)
                FileUtil.deleteFile(backupTempFilePath)
                callback.invoke(true, "")
            }
            catch (e: Exception)
            {
                FileUtil.deleteFile(backupTempFilePath)
                callback.invoke(false, AppConstant.RESTORE_DATA_FAILED_WRONG_BACKUP_FILE)
            }
        }
    }

    /**
     * 获取备份文件列表
     *
     * @return 文件路径列表
     */
    fun getBackupFileList(): List<File>
    {
        val fileList = mutableListOf<File>()

        val resolver = UiApplication.instance.contentResolver
        val uri = MediaStore.Files.getContentUri("external")
        val cursor = resolver.query(uri, arrayOf(MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.SIZE),
                MediaStore.Files.FileColumns.DATA + " LIKE '%" + backupFileSuffix + "%'", null, null)
        while (cursor?.moveToNext() == true)
        {
            val file = File(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)))
            if (file.exists()) fileList.add(file)
        }
        cursor?.close()

        return fileList
    }

    class BackupJob(val backupFileName: String, val callback: (result: Boolean) -> Unit)
}