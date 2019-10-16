package com.lizl.mydiary.util

import android.net.Uri
import android.os.Environment
import com.lizl.mydiary.UiApplication
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

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
            val systemFilePath =
                    if ((Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()) && UiApplication.instance.getExternalFilesDir(
                                    null) != null)
                    {
                        UiApplication.instance.getExternalFilesDir(null)!!.path
                    }
                    else
                    {
                        UiApplication.instance.filesDir.path
                    }

            val systemFileDir = File(systemFilePath)
            if (!systemFileDir.exists())
            {
                systemFileDir.mkdirs()
            }

            return systemFilePath
        }

        /**
         * 根据Uri获取真实的文件路径
         *
         * @param context
         * @param uri
         * @return
         */
        fun getFilePathFromUri(uri: Uri?): String?
        {
            if (uri == null) return null

            val resolver = UiApplication.instance.contentResolver
            var input: FileInputStream? = null
            var output: FileOutputStream? = null
            try
            {
                val pfd = resolver.openFileDescriptor(uri, "r")!!
                input = FileInputStream(pfd.fileDescriptor)

                val outputFile = File.createTempFile("image", "tmp", UiApplication.instance.cacheDir)
                val tempFilename = outputFile.absolutePath
                output = FileOutputStream(tempFilename)

                var read = 0
                val bytes = ByteArray(4096)
                while ({ read = input.read(bytes);read }() != -1)
                {
                    output.write(bytes, 0, read)
                }

                return File(tempFilename).absolutePath
            }
            catch (ignored: Exception)
            {

                ignored.stackTrace
            } finally
            {
                try
                {
                    input?.close()
                    output?.close()
                }
                catch (t: Throwable)
                {
                    // Do nothing
                }
            }
            return null
        }

        /**
         * 删除文件
         */
        fun deleteFile(filePath: String): Boolean
        {
            val file = File(filePath)
            if (!file.exists())
            {
                return true
            }
            return file.delete()
        }
    }
}