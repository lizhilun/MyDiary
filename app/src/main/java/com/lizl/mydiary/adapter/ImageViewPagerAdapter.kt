package com.lizl.mydiary.adapter

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.github.chrisbanes.photoview.PhotoView

class ImageViewPagerAdapter(private val imageList: List<String>) : PagerAdapter()
{
    override fun isViewFromObject(view: View, obj: Any): Boolean
    {
        return view == obj
    }

    override fun getCount() = imageList.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any
    {
        return PhotoView(container.context).apply {
            this.setImageURI(Uri.parse(imageList[position]))
            container.addView(this)
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any)
    {
        container.removeView(obj as View)
    }

    override fun getItemPosition(`object`: Any) = POSITION_NONE
}