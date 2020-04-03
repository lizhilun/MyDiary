package com.lizl.mydiary.util

import android.widget.ImageView
import com.bumptech.glide.request.target.Target
import com.lizl.mydiary.GlideApp

object GlideUtil
{
    /**
     * 加载图片并显示
     */
    fun displayImage(imageView: ImageView, imageUri: String)
    {
        GlideApp.with(imageView).load(imageUri).into(imageView)
    }

    /**
     * 加载原图并显示
     */
    fun displayOriImage(imageView: ImageView, imageUri: Any)
    {
        GlideApp.with(imageView).load(imageUri).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).into(imageView)
    }
}