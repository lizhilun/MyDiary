package com.lizl.mydiary.adapter

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.mydiary.R
import com.lizl.mydiary.util.GlideUtil
import kotlinx.android.synthetic.main.item_image_pager.view.*

class ImagePagerAdapter(imageList: MutableList<String>) : BaseQuickAdapter<String, ImagePagerAdapter.ViewHolder>(R.layout.item_image_pager, imageList)
{
    override fun convert(helper: ViewHolder, item: String)
    {
        helper.bindViewHolder(item)
    }

    class ViewHolder(itemView: View) : BaseViewHolder(itemView)
    {
        fun bindViewHolder(imageUrl: String)
        {
            GlideUtil.displayImage(itemView.iv_image, imageUrl)
        }
    }
}