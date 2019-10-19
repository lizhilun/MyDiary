package com.lizl.mydiary.util

import android.net.Uri
import android.util.Log
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.PathUtils
import com.lizl.mydiary.UiApplication
import java.io.*

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
            }
            finally
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
            var flag = true
            val thisFile = File(filePath)
            try
            {
                if (!thisFile.parentFile.exists())
                {
                    thisFile.parentFile.mkdirs()
                }
                val fw = FileWriter(filePath, false)
                fw.write(content)
                fw.close()
            }
            catch (e: IOException)
            {
                flag = false
                Log.e(TAG, e.toString())
            }
            return flag
        }

        /**
         * 读TXT文件内容
         * @param filePath 文件绝对路径
         */
        @Throws(Exception::class)
        fun readTxtFile(filePath: String): String
        {
            var result = ""
            val txtFile = File(filePath)
            var fileReader: FileReader? = null
            var bufferedReader: BufferedReader? = null
            try
            {
                fileReader = FileReader(txtFile)
                bufferedReader = BufferedReader(fileReader)
                try
                {
                    var read: String? = null
                    while ({ read = bufferedReader.readLine();read }() != null)
                    {
                        result = result + read + "\r\n"
                    }
                }
                catch (e: Exception)
                {
                    e.printStackTrace()
                }
            }
            catch (e: Exception)
            {
                e.printStackTrace()
            }
            finally
            {
                bufferedReader?.close()
                fileReader?.close()
            }
            return result
        }

        fun copyDir(srcDir: String, dstDir: String): Boolean
        {
            return FileUtils.copyDir(srcDir, dstDir)
        }
    }
}