package com.lizl.mydiary.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lizl.mydiary.R
import com.lizl.mydiary.util.GlideUtil
import kotlinx.android.synthetic.main.item_diary_image.view.*

class DiaryImageListAdapter(private var editable: Boolean, private val maxImageCount: Int) : RecyclerView.Adapter<DiaryImageListAdapter.ViewHolder>()
{
    private val VIEW_TYPE_ADD_BTN = 1
    private val VIEW_TYPE_IMAGE = 2

    private lateinit var context: Context

    private val imageList = mutableListOf<String>()

    private var onAddImageBtnClickListener: (() -> Unit)? = null
    private var onImageClickListener: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        if (!this::context.isInitialized)
        {
            context = parent.context
        }
        return when (viewType)
        {
            VIEW_TYPE_IMAGE -> ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_diary_image, parent, false))
            else            -> ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_diary_add_image_btn, parent, false))
        }
    }

    override fun getItemCount(): Int
    {
        return if (editable)
        {
            (imageList.size + 1).coerceAtMost(maxImageCount)
        }
        else
        {
            (imageList.size).coerceAtMost(maxImageCount)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        when (getItemViewType(position))
        {
            VIEW_TYPE_IMAGE   -> holder.bindImageViewHolder(imageList[position])
            VIEW_TYPE_ADD_BTN -> holder.bindAddBtnViewHolder()
        }
    }

    override fun getItemViewType(position: Int): Int
    {
        return if (position == imageList.size) VIEW_TYPE_ADD_BTN else VIEW_TYPE_IMAGE
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        fun bindImageViewHolder(imageUrl: String)
        {
            GlideUtil.displayImage(context, imageUrl, itemView.iv_image)

            itemView.setOnClickListener {
                onImageClickListener?.invoke(imageUrl)
            }
        }

        fun bindAddBtnViewHolder()
        {
            itemView.setOnClickListener {
                onAddImageBtnClickListener?.invoke()
            }
        }
    }

    fun setOnAddImageBtnClickListener(onAddImageBtnClickListener: () -> Unit)
    {
        this.onAddImageBtnClickListener = onAddImageBtnClickListener
    }

    fun setOnImageClickListener(onImageClickListener: (String) -> Unit)
    {
        this.onImageClickListener = onImageClickListener
    }

    fun addImageList(imageList: List<String>)
    {
        this.imageList.addAll(imageList)
        notifyDataSetChanged()
    }

    fun setEditable(editable: Boolean)
    {
        this.editable = editable
        notifyDataSetChanged()
    }

    fun getImageList() = imageList
}