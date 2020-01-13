package com.lizl.mydiary.util

import android.util.Log
import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.exception.ZipException
import net.lingala.zip4j.model.ZipParameters
import net.lingala.zip4j.model.enums.CompressionLevel
import net.lingala.zip4j.model.enums.CompressionMethod
import net.lingala.zip4j.model.enums.EncryptionMethod
import java.io.File

object ZipUtil
{
    private val TAG = "ZipUtil"

    const val UNZIP_SUCCESS = 0
    const val UNZIP_FAILED_WRONG_PASSWORD = 1
    const val UNZIP_FAILED_WRONG_ZIP_FILE = 2

    fun zipFile(oriFilePath: String, zipFilePath: String, password: String): Boolean
    {
        try
        {
            val oriFile = File(oriFilePath)
            val zipFile = ZipFile(zipFilePath)
            val zipParameters = ZipParameters()

            zipParameters.compressionMethod = CompressionMethod.DEFLATE
            zipParameters.compressionLevel = CompressionLevel.FASTEST
            if (password.isNotEmpty())
            {
                zipParameters.isEncryptFiles = true
                zipParameters.encryptionMethod = EncryptionMethod.ZIP_STANDARD
                zipFile.setPassword(password.toCharArray())
            }
            if (oriFile.isDirectory)
            {
                zipFile.addFolder(oriFile, zipParameters)
            }
            else
            {
                zipFile.addFile(oriFile, zipParameters)
            }
        }
        catch (e: Exception)
        {
            Log.e(TAG, "zipFile error:", e)
            return false
        }
        return true
    }

    fun unZipFile(zipFilePath: String, dstFilePath: String, password: String): Int
    {
        try
        {
            val zipFile = ZipFile(zipFilePath)
            if (zipFile.isEncrypted)
            {
                zipFile.setPassword(password.toCharArray())
            }
            zipFile.extractAll(dstFilePath)
        }
        catch (e: ZipException)
        {
            Log.e(TAG, "unZipFile error:", e)
            if (e.type == ZipException.Type.WRONG_PASSWORD)
            {
                return UNZIP_FAILED_WRONG_PASSWORD
            }
            return UNZIP_FAILED_WRONG_ZIP_FILE
        }

        return UNZIP_SUCCESS
    }
}