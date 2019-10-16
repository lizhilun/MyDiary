package com.lizl.mydiary.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class ImageUtil
{
    companion object
    {
        /**
         * 根据路径获得突破并压缩返回bitmap用于显示
         *
         * @return
         */
        fun getSmallBitmap(filePath: String, newWidth: Int, newHeight: Int): Bitmap?
        {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(filePath, options)

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, newWidth, newHeight)

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false

            val bitmap = BitmapFactory.decodeFile(filePath, options)
            val newBitmap = compressImage(bitmap!!, 500)
            bitmap.recycle()
            return newBitmap
        }

        /**
         * 计算图片的缩放值
         *
         * @param options
         * @param reqWidth
         * @param reqHeight
         * @return
         */
        private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int
        {
            // Raw height and width of image
            val height = options.outHeight
            val width = options.outWidth
            var inSampleSize = 1

            if (height > reqHeight || width > reqWidth)
            {
                val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
                val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
                inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
            }

            return inSampleSize
        }

        /**
         * 质量压缩
         * @param image
         * @param maxSize
         */
        private fun compressImage(image: Bitmap, maxSize: Int): Bitmap?
        {
            val os = ByteArrayOutputStream()
            // scale
            var options = 80
            // Store the bitmap into output stream(no compress)
            image.compress(Bitmap.CompressFormat.JPEG, options, os)
            // Compress by loop
            while (os.toByteArray().size / 1024 > maxSize)
            {
                // Clean up os
                os.reset()
                // interval 10
                options -= 10
                image.compress(Bitmap.CompressFormat.JPEG, options, os)
            }

            var bitmap: Bitmap? = null
            val b = os.toByteArray()
            if (b.isNotEmpty())
            {
                bitmap = BitmapFactory.decodeByteArray(b, 0, b.size)
            }
            return bitmap
        }

        /**
         * 图片保存到SD卡
         * @param bitmap
         * @return
         */
        fun saveImageToSdCard(bitmap: Bitmap): String
        {
            val imageSavePath = FileUtil.getSystemFilePath() + "/picture/"
            val imageSaveDir = File(imageSavePath)
            if (!imageSaveDir.exists())
            {
                imageSaveDir.mkdirs()
            }
            val imageUrl = imageSavePath + System.currentTimeMillis() + ".jpg"
            val file = File(imageUrl)
            try
            {
                val out = FileOutputStream(file)
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out))
                {
                    out.flush()
                    out.close()
                }
            }
            catch (e: java.lang.Exception)
            {
                Log.e(FileUtil.TAG, "saveImageToSdCard error " + e.message)
            }

            return file.absolutePath
        }

    }
}