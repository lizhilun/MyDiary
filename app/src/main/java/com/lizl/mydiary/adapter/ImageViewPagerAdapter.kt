package com.lizl.mydiary.adapter

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.mydiary.R
import com.lizl.mydiary.util.GlideUtil
import kotlinx.android.synthetic.main.item_photo_view.view.*

class ImageViewPagerAdapter(imageList: List<String>) :
    BaseQuickAdapter<String, ImageViewPagerAdapter.ViewHolder>(R.layout.item_photo_view, imageList.toMutableList())
{

    override fun convert(helper: ViewHolder, item: String)
    {
        helper.bindViewHolder(item)
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView)
    {
        fun bindViewHolder(imagePath: String)
        {
            GlideUtil.displayOriImage(context, imagePath, itemView.photo_view)
        }
    }
}