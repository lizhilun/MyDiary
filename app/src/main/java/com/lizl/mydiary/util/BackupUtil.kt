package com.lizl.mydiary.util

import android.provider.MediaStore
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.ZipUtils
import com.lizl.mydiary.UiApplication
import com.lizl.mydiary.bean.DiaryBean
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class BackupUtil
{
    companion object
    {
        private val backupFilePath = PathUtils.getExternalStoragePath() + "/DiaryBackup"
        private val backupTempFilePath = "$backupFilePath/temp"
        private val backupTempImageFilePath = "$backupFilePath/temp/picture"
        private val backupTempDiaryFilePath = "$backupFilePath/temp/diary.txt"
        private const val backupFileSuffix = ".iui"

        fun backupData(callback: (result: Boolean) -> Unit)
        {
            GlobalScope.launch {

                try
                {
                    val diaryList = AppDatabase.instance.getDiaryDao().getAllDiary()
                    val diaryListText = GsonUtils.toJson(diaryList)
                    val writeResult = FileUtil.writeTxtFile(diaryListText, backupTempDiaryFilePath)
                    if (!writeResult)
                    {
                        FileUtils.deleteDir(backupTempFilePath)
                        callback.invoke(false)
                        return@launch
                    }
                    val imageFileCopyResult = FileUtil.copyDir(FileUtil.getImageFileSavePath(), backupTempImageFilePath)
                    if (!imageFileCopyResult)
                    {
                        FileUtils.deleteDir(backupTempFilePath)
                        callback.invoke(false)
                        return@launch
                    }
                    val formatter = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                    val backupFileName = formatter.format(System.currentTimeMillis()) + backupFileSuffix
                    val zipResult = ZipUtils.zipFile(backupTempFilePath, "$backupFilePath/$backupFileName")
                    FileUtils.deleteDir(backupTempFilePath)
                    callback.invoke(zipResult)

                    FileUtils.notifySystemToScan(backupFilePath)
                }
                catch (e: Exception)
                {
                    callback.invoke(false)
                }
            }
        }

        fun restoreData(restoreFilePath: String, callback: (result: Boolean) -> Unit)
        {
            GlobalScope.launch {

                try
                {
                    ZipUtils.unzipFile(restoreFilePath, backupFilePath)
                    val diaryTxtFile = File(backupTempDiaryFilePath)
                    if (!diaryTxtFile.exists())
                    {
                        callback.invoke(false)
                        return@launch
                    }
                    val diaryTxt = FileUtil.readTxtFile(backupTempDiaryFilePath)
                    FileUtil.copyDir(backupTempImageFilePath, FileUtil.getImageFileSavePath())
                    val diaryList = GsonUtils.fromJson<Array<DiaryBean>>(diaryTxt, Array<DiaryBean>::class.java)
                    val saveDiaryList = mutableListOf<DiaryBean>()
                    for (diaryBean in diaryList)
                    {
                        val existBean = AppDatabase.instance.getDiaryDao().getDiaryByCreateTime(diaryBean.createTime)
                        if (existBean == null)
                        {
                            saveDiaryList.add(diaryBean)
                        }
                    }
                    AppDatabase.instance.getDiaryDao().insertList(saveDiaryList)
                    callback.invoke(true)
                }
                catch (e: Exception)
                {
                    callback.invoke(false)
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
            if (cursor != null)
            {
                while (cursor.moveToNext())
                {
                    val file = File(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)))
                    if (file.exists()) fileList.add(file)
                }
            }
            cursor?.close()

            return fileList
        }
    }
}