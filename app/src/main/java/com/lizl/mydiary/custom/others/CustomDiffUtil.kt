package com.lizl.mydiary.custom.others

import androidx.recyclerview.widget.DiffUtil

class CustomDiffUtil<T : Any>(private val areItemsTheSame: (T, T) -> Boolean, private val areContentsTheSame: (T, T) -> Boolean) : DiffUtil.ItemCallback<T>()
{
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean
    {
        return areItemsTheSame.invoke(oldItem, newItem)
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean
    {
        return areContentsTheSame.invoke(oldItem, newItem)
    }
}