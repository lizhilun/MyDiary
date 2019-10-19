package com.lizl.mydiary.util

import android.graphics.Bitmap
import android.net.Uri
import com.blankj.utilcode.util.*
import java.io.File
import java.io.IOException

class FileUtil
{
    companion object
    {
        val TAG = "FileUtil"

        /**
         * 获取系统文件路径
         */
        fun getSystemFilePath(): String
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
         * 获取图片保存路径
         */
        fun getImageFileSavePath() = "${getSystemFilePath()}/picture/"

        /**
         * 根据Uri获取真实的文件路径
         *
         * @param context
         * @param uri
         * @return
         */
        fun getFilePathFromUri(uri: Uri): String?
        {
            return UriUtils.uri2File(uri)?.absolutePath
        }

        /**
         * 保存图片
         */
        fun saveImageToData(imagePath: String): String
        {
            val bitmap = ImageUtils.getBitmap(imagePath)
            val comBitmap = ImageUtils.compressByQuality(bitmap, 90)
            val savePath = "${getSystemFilePath()}/${System.currentTimeMillis()}.jpg"
            ImageUtils.save(comBitmap, savePath, Bitmap.CompressFormat.JPEG)
            return savePath
        }

        /**
         * 删除文件
         */
        fun deleteFile(filePath: String): Boolean
        {
            return FileUtils.delete(filePath)
        }

        /**
         * 写文件
         *
         * @param content 文件内容
         * @param filePath 文件绝对路径
         *
         * @throws IOException
         */
        fun writeTxtFile(content: String, filePath: String): Boolean
        {
            return FileIOUtils.writeFileFromString(filePath, content)
        }

        /**
         * 读TXT文件内容
         * @param filePath 文件绝对路径
         */
        @Throws(Exception::class)
        fun readTxtFile(filePath: String): String
        {
            return FileIOUtils.readFile2String(filePath)
        }

        fun copyDir(srcDir: String, dstDir: String): Boolean
        {
            return FileUtils.copyDir(srcDir, dstDir)
        }

        fun isFileExists(filePath: String): Boolean
        {
            return FileUtils.isFileExists(filePath)
        }
    }
}