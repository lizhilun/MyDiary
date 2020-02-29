package com.lizl.mydiary.util

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.content.FileProvider
import com.blankj.utilcode.util.*
import java.io.File
import java.util.*

object FileUtil
{
    private const val TAG = "FileUtil";

    /**
     * 获取系统文件路径
     */
    private fun getSystemFilePath(): String
    {
        val systemFilePath = PathUtils.getInternalAppFilesPath()

        val systemFileDir = File(systemFilePath)
        if (!systemFileDir.exists())
        {
            systemFileDir.mkdirs()
        }

        return systemFilePath
    }

    /**
     * 获取系统缓存路径
     */
    fun getSystemCachePath(): String = PathUtils.getInternalAppCachePath()

    /**
     * 获取图片保存路径
     */
    fun getImageFileSavePath() = "${getSystemFilePath()}/picture/"

    /**
     * 根据Uri获取真实的文件路径
     */
    fun getFilePathFromUri(uri: Uri) = UriUtils.uri2File(uri)?.absolutePath

    /**
     * 删除文件
     */
    fun deleteFile(file: File) = FileUtils.delete(file)

    /**
     * 删除文件
     */
    fun deleteFile(filePath: String) = FileUtils.delete(filePath)

    /**
     * 重命名文件
     */
    fun renameFile(file: File, newName: String, needNotify: Boolean): Boolean
    {
        if (!file.exists() || newName.isBlank())
        {
            return false
        }

        if (file.nameWithoutExtension == newName)
        {
            return true
        }

        val newFilePath = file.absolutePath.replace(file.nameWithoutExtension, newName)
        val result = file.renameTo(File(newFilePath))
        if (needNotify)
        {
            FileUtils.notifySystemToScan(newFilePath)
        }
        return result
    }

    /**
     * 写文件
     */
    fun writeTxtFile(content: String, filePath: String) = FileIOUtils.writeFileFromString(filePath, content)

    /**
     * 读TXT文件内容
     */
    fun readTxtFile(filePath: String): String = FileIOUtils.readFile2String(filePath)

    /**
     * 复制文件
     */
    fun copyDir(srcDir: String, dstDir: String): Boolean = FileUtils.copyDir(srcDir, dstDir)

    /**
     * 判断文件是否存在
     */
    fun isFileExists(filePath: String): Boolean = FileUtils.isFileExists(filePath)

    /**
     * 获取文件大小
     */
    fun getFileSize(file: File): String
    {
        val len = FileUtils.getFileLength(file)
        return when
        {
            len < 0                  -> "0B"
            len < 1024               -> String.format(Locale.getDefault(), "%.1fB", len.toDouble())
            len < 1024 * 1024        -> String.format(Locale.getDefault(), "%.1fKB", len.toDouble() / 1024)
            len < 1024 * 1024 * 1024 -> String.format(Locale.getDefault(), "%.1fMB", len.toDouble() / (1024 * 1024))
            else                     -> String.format(Locale.getDefault(), "%.1fGB", len.toDouble() / (1024 * 1024 * 1024))
        }
    }

    /**
     * 获取文件uri
     */
    fun getFileUri(file: File): Uri
    {
        return FileProvider.getUriForFile(ActivityUtils.getTopActivity(), "com.lizl.mydiary.fileprovider", file);
    }

    /**
     * 分享所有类型的文件
     */
    fun shareAllTypeFile(file: File)
    {
        try
        {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_STREAM, getFileUri(file))
            shareIntent.type = "*/*"; //此处可发送多种文件
            ActivityUtils.getTopActivity().startActivity(Intent.createChooser(shareIntent, ""))
        }
        catch (e: Exception)
        {
            Log.e(TAG, "shareAllTypeFile error:", e)
        }
    }
}