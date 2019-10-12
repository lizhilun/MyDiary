package com.lizl.mydiary.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.InputStream

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
        fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int
        {
            // Raw height and width of image
            val height = options.outHeight
            val width = options.outWidth
            var inSampleSize = 1

            if (height > reqHeight || width > reqWidth)
            {

                // Calculate ratios of height and width to requested height and
                // width
                val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
                val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())

                // Choose the smallest ratio as inSampleSize value, this will
                // guarantee
                // a final image with both dimensions larger than or equal to the
                // requested height and width.
                inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
            }

            return inSampleSize
        }

        /**
         * 把byte数据解析成图片
         */
        private fun decodeBitmap(path: String?, data: ByteArray?, context: Context, uri: Uri?, options: BitmapFactory.Options): Bitmap?
        {
            var result: Bitmap? = null
            if (path != null)
            {
                result = BitmapFactory.decodeFile(path, options)
            }
            else if (data != null)
            {
                result = BitmapFactory.decodeByteArray(data, 0, data.size, options)
            }
            else if (uri != null)
            {
                val cr = context.contentResolver
                var inputStream: InputStream? = null
                try
                {
                    inputStream = cr.openInputStream(uri)
                    result = BitmapFactory.decodeStream(inputStream, null, options)
                    inputStream!!.close()
                }
                catch (e: Exception)
                {
                    e.printStackTrace()
                }

            }
            return result
        }

        /**
         * 质量压缩
         * @param image
         * @param maxSize
         */
        fun compressImage(image: Bitmap, maxSize: Int): Bitmap?
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
            if (b.size != 0)
            {
                bitmap = BitmapFactory.decodeByteArray(b, 0, b.size)
            }
            return bitmap
        }

    }
}