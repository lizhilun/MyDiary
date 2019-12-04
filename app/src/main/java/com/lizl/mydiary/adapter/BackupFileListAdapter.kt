package com.lizl.mydiary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lizl.mydiary.R
import com.lizl.mydiary.util.FileUtil
import kotlinx.android.synthetic.main.item_backup_file.view.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class BackupFileListAdapter : BaseAdapter<File, BackupFileListAdapter.ViewHolder>()
{
    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    private var onFileItemClickListener: ((File) -> Unit)? = null

    override fun createCustomViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_backup_file, parent, false))
    }

    override fun bindCustomViewHolder(holder: ViewHolder, bean: File, position: Int)
    {
        holder.bindViewHolder(bean)
    }

    override fun getCustomItemViewType(position: Int) = 0

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView)
    {
        fun bindViewHolder(file: File)
        {
            itemView.tv_file_name.text = file.name
            itemView.tv_file_size.text = FileUtil.getFileSize(file)
            itemView.tv_file_time.text = formatter.format(file.lastModified())

            itemView.setOnClickListener { onFileItemClickListener?.invoke(file) }
        }
    }

    fun addAll(fileList: List<File>)
    {
        super.addAll(fileList.sortedByDescending { it.lastModified() })
    }

    fun setOnFileItemClickListener(onFileItemClickListener: (File) -> Unit)
    {
        this.onFileItemClickListener = onFileItemClickListener
    }
}