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
    private lateinit var mContext: Context
    private var mFooter: View? = null
    private lateinit var mRecyclerView: RecyclerView
    private var mData: MutableList<T> = ArrayList()

    override fun getItemCount(): Int
    {
        return if (mData.size != 0) if (mFooter == null) mData.size else mData.size + 1 else 0
    }

    override fun getItemViewType(position: Int): Int
    {
        return if (this.getFooter() != null && this.itemCount - 1 == position) 233333 else super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder
    {
        if (!this::mContext.isInitialized)
        {
            mContext = parent.context
        }

        if (!this::mRecyclerView.isInitialized)
        {
            mRecyclerView = parent as RecyclerView
        }

        return this.createCustomViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int)
    {
        if (this.getItemViewType(position) != 233333)
        {
            this.bindCustomViewHolder(holder as VH, mData[position], position)
        }
    }

    fun inflateView(@LayoutRes resId: Int, parent: ViewGroup): View
    {
        return LayoutInflater.from(mContext).inflate(resId, parent, false)
    }

    fun getContext(): Context
    {
        return mContext
    }

    private fun getFooter(): View?
    {
        return mFooter
    }

    fun setFooter(footer: View)
    {
        mFooter = footer
    }

    fun removeFooter()
    {
        mFooter = null
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

    @SafeVarargs fun addAll(vararg items: T)
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

    private fun scrollTop()
    {
        mRecyclerView.scrollToPosition(0)
    }

    abstract fun createCustomViewHolder(var1: ViewGroup, var2: Int): VH

    abstract fun bindCustomViewHolder(var1: VH, var2: T, var3: Int)
}