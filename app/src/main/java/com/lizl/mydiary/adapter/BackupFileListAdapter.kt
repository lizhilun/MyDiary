package com.lizl.mydiary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lizl.mydiary.R
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

            itemView.tv_file_time.text = formatter.format(file.lastModified())
            itemView.setOnClickListener { onFileItemClickListener?.invoke(file) }
        }
    }

    interface OnBackFileItemClickListener
    {
        fun onBackupFileItemClick(file: File)
    }

    fun addAll(fileList: List<File>)
    {
        Collections.sort(fileList, FileComparator())
        super.addAll(fileList)
    }

    fun setOnFileItemClickListener(onFileItemClickListener: (File) -> Unit)
    {
        this.onFileItemClickListener = onFileItemClickListener
    }

    inner class FileComparator : Comparator<File>
    {
        override fun compare(file1: File, file2: File): Int
        {
            return if (file1.lastModified() > file2.lastModified()) -1 else 1
        }
    }
}