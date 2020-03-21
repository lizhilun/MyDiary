package com.lizl.mydiary.util

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.request.target.Target
import com.lizl.mydiary.GlideApp

object GlideUtil
{
    /**
     * 加载图片并显示
     */
    fun displayImage(context: Context, imageUri: String, imageView: ImageView)
    {
        GlideApp.with(context).load(imageUri).into(imageView)
    }

    /**
     * 加载原图并显示
     */
    fun displayOriImage(context: Context, imageUri: String, imageView: ImageView)
    {
        GlideApp.with(context).load(imageUri).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).into(imageView)
    }
}