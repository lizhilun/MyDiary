package com.lizl.mydiary.adapter

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.github.chrisbanes.photoview.PhotoView
import com.lizl.mydiary.util.GlideUtil

class ImageViewPagerAdapter(private val imageList: List<String>) : PagerAdapter()
{
    override fun isViewFromObject(view: View, obj: Any) = view == obj

    override fun getCount() = imageList.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any
    {
        return PhotoView(container.context).apply {
            GlideUtil.displayOriImage(context, imageList[position], this)
            container.addView(this)
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any)
    {
        container.removeView(obj as View)
    }

    override fun getItemPosition(`object`: Any) = POSITION_NONE
}