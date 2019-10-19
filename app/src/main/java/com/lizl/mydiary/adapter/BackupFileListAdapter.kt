package com.lizl.mydiary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lizl.mydiary.R
import kotlinx.android.synthetic.main.item_backup_file.view.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class BackupFileListAdapter(private val fileList: List<File>, private val onItemClickListener: OnBackFileItemClickListener?) : RecyclerView.Adapter<BackupFileListAdapter.ViewHolder>()
{
    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    init
    {
        Collections.sort(fileList, FileComparator())
    }

    override fun getItemCount(): Int = fileList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_backup_file, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.bindViewHolder(fileList[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        fun bindViewHolder(file: File)
        {
            itemView.tv_file_name.text = file.name

            itemView.tv_file_time.text = formatter.format(file.lastModified())
            itemView.setOnClickListener { onItemClickListener?.onBackupFileItemClick(file) }
        }
    }

    interface OnBackFileItemClickListener
    {
        fun onBackupFileItemClick(file: File)
    }

    inner class FileComparator : Comparator<File>
    {
        override fun compare(file1: File, file2: File): Int
        {
            return if (file1.lastModified() > file2.lastModified()) -1 else 1
        }
    }
}