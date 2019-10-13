package com.lizl.mydiary.util

import android.content.Context
import android.widget.ImageView
import com.lizl.mydiary.GlideApp

class GlideUtil
{
    companion object
    {
        /**
         * 加载图片并显示
         */
        fun displayImage(context: Context, imageUri: String, imageView: ImageView)
        {
            GlideApp.with(context).load(imageUri).into(imageView)
        }
    }
}