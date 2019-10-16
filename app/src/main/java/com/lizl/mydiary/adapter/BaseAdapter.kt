package com.lizl.mydiary.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import java.util.*

abstract class BaseAdapter<T, VH : BaseViewHolder> : RecyclerView.Adapter<BaseViewHolder>()
{
    protected val TAG = this.javaClass.simpleName

    private val mLock = Any()
    protected lateinit var context: Context
    protected lateinit var recyclerView: RecyclerView
    private var footer: View? = null
    private var header: View? = null
    private var mData: MutableList<T> = ArrayList()

    protected val VIEW_TYPE_UNKNOW = 0
    protected val VIEW_TYPE_FOOTER = 2333
    protected val VIEW_TYPE_HEADER = 2444

    override fun getItemCount() = mData.size + (if (footer != null) 1 else 0) + (if (header != null) 1 else 0)

    override fun getItemViewType(position: Int): Int
    {
        if (header != null && position == 0)
        {
            return VIEW_TYPE_HEADER
        }
        else if (footer != null && position == itemCount - 1)
        {
            return VIEW_TYPE_FOOTER
        }
        return getCustomItemViewType(if (header != null) position - 1 else position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder
    {
        if (!this::context.isInitialized)
        {
            context = parent.context
        }

        if (!this::recyclerView.isInitialized)
        {
            recyclerView = parent as RecyclerView
        }

        return when (viewType)
        {
            VIEW_TYPE_HEADER -> BaseViewHolder(header!!)
            VIEW_TYPE_FOOTER -> BaseViewHolder(footer!!)
            else             -> createCustomViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int)
    {
        if (getItemViewType(position) == VIEW_TYPE_HEADER || getItemViewType(position) == VIEW_TYPE_FOOTER)
        {
            return
        }
        if (header != null)
        {
            bindCustomViewHolder(holder as VH, mData[position - 1], position - 1)
        }
        else
        {
            bindCustomViewHolder(holder as VH, mData[position], position)
        }
    }

    fun inflateView(@LayoutRes
                    resId: Int, parent: ViewGroup): View
    {
        return LayoutInflater.from(context).inflate(resId, parent, false)
    }

    fun setFooter(footer: View)
    {
        this.footer = footer
    }

    fun removeFooter()
    {
        footer = null
        notifyDataSetChanged()
    }

    fun setHeader(header: View)
    {
        this.header = header
    }

    fun removeHeader()
    {
        header = null
        notifyDataSetChanged()
    }

    fun add(`object`: T)
    {
        synchronized(this.mLock) {
            mData.add(`object`)
            this.notifyItemInserted(mData.size - 1)
        }
    }

    fun addAll(collection: Collection<T>)
    {
        synchronized(this.mLock) {
            mData.addAll(collection)
            if (mData.size - collection.size != 0)
            {
                this.notifyItemRangeInserted(mData.size - collection.size, collection.size)
            }
            else
            {
                this.notifyDataSetChanged()
            }
        }
    }

    @SafeVarargs
    fun addAll(vararg items: T)
    {
        synchronized(this.mLock) {
            Collections.addAll<T>(mData, *items)
            if (mData.size - items.size != 0)
            {
                this.notifyItemRangeInserted(mData.size - items.size, items.size)
            }
            else
            {
                this.notifyDataSetChanged()
            }

        }
    }

    fun insert(`object`: T, index: Int)
    {
        if (index >= 0 && index <= mData.size)
        {
            synchronized(this.mLock) {
                mData.add(index, `object`)
                this.notifyItemInserted(index)
                if (index == 0)
                {
                    this.scrollTop()
                }
            }
        }
        else
        {
            Log.i(TAG, "insert: index error")
        }
    }

    fun insertAll(collection: Collection<T>, index: Int)
    {
        if (index >= 0 && index <= mData.size)
        {
            synchronized(this.mLock) {
                mData.addAll(index, collection)
                this.notifyItemRangeInserted(index, collection.size)
                if (index == 0)
                {
                    this.scrollTop()
                }
            }
        }
        else
        {
            Log.i(TAG, "insertAll: index error")
        }
    }

    fun remove(index: Int)
    {
        if (index >= 0 && index <= mData.size - 1)
        {
            synchronized(this.mLock) {
                mData.removeAt(index)
                this.notifyItemRemoved(index)
            }
        }
        else
        {
            Log.i(TAG, "remove: index error")
        }
    }

    fun remove(`object`: T): Boolean
    {
        var removeSuccess: Boolean
        if (mData.size != 0)
        {
            val removeIndex: Int
            synchronized(this.mLock) {
                removeIndex = mData.indexOf(`object`)
                removeSuccess = mData.remove(`object`)
            }

            return if (removeSuccess)
            {
                this.notifyItemRemoved(removeIndex)
                true
            }
            else
            {
                false
            }
        }
        else
        {
            Log.i(TAG, "remove fail datas emply")
            return false
        }
    }

    fun clear()
    {
        synchronized(this.mLock) {
            mData.clear()
        }

        this.notifyDataSetChanged()
    }

    fun sort(comparator: Comparator<in T>)
    {
        synchronized(this.mLock) {
            Collections.sort<T>(mData, comparator)
        }

        this.notifyDataSetChanged()
    }

    fun update(mData: MutableList<T>)
    {
        synchronized(this.mLock) {
            this.mData = mData
        }

        this.notifyDataSetChanged()
    }

    fun getItem(position: Int): T
    {
        return mData[position]
    }

    fun getPosition(item: T): Int
    {
        return mData.indexOf(item)
    }

    fun getData(): List<T>
    {
        return mData
    }

    fun getFooter() = footer

    private fun scrollTop()
    {
        recyclerView.scrollToPosition(0)
    }

    abstract fun createCustomViewHolder(parent: ViewGroup, viewType: Int): VH

    abstract fun bindCustomViewHolder(holder: VH, bean: T, position: Int)

    abstract fun getCustomItemViewType(position: Int): Int
}