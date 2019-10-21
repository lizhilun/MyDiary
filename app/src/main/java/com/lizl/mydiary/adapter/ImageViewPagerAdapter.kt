package com.lizl.mydiary.adapter

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.viewpager.widget.PagerAdapter
import com.lizl.mydiary.util.GlideUtil

class ImageViewPagerAdapter(private val imageList: List<String>) : PagerAdapter()
{
    override fun isViewFromObject(view: View, obj: Any): Boolean
    {
        return view == obj
    }

    override fun getCount() = imageList.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any
    {
        val imageView = AppCompatImageView(container.context)

        GlideUtil.displayImage(container.context, imageList[position], imageView)

        container.addView(imageView)
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any)
    {
        container.removeView(obj as View)
    }

    override fun getItemPosition(`object`: Any) = POSITION_NONE
}