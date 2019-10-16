package com.lizl.mydiary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lizl.mydiary.R
import com.lizl.mydiary.bean.SettingBean
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.item_setting_boolean.view.*
import kotlinx.android.synthetic.main.item_setting_normal.view.*

class SettingListAdapter(private val settingList: List<SettingBean.SettingBaseBean>) : RecyclerView.Adapter<SettingListAdapter.ViewHolder>()
{
    companion object
    {
        const val ITEM_TYPE_DIVIDE = 1
        const val ITEM_TYPE_BOOLEAN = 2
        const val ITEM_TYPE_NORMAL = 3
    }

    override fun getItemCount(): Int = settingList.size

    override fun getItemViewType(position: Int): Int
    {
        if (position >= itemCount)
        {
            return -1
        }
        return when
        {
            settingList[position] is SettingBean.SettingDivideBean  -> ITEM_TYPE_DIVIDE
            settingList[position] is SettingBean.SettingBooleanBean -> ITEM_TYPE_BOOLEAN
            settingList[position] is SettingBean.SettingNormalBean  -> ITEM_TYPE_NORMAL
            else                                                    -> -1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        when (viewType)
        {
            ITEM_TYPE_BOOLEAN -> return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_setting_boolean, parent, false))
            ITEM_TYPE_DIVIDE  -> return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_setting_divide, parent, false))
            ITEM_TYPE_NORMAL  -> return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_setting_normal, parent, false))
        }
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_setting_divide, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val settingItem = settingList[position]
        if (settingItem is SettingBean.SettingBooleanBean)
        {
            holder.bindBooleanViewHolder(settingItem, position)
        }
        else if (settingItem is SettingBean.SettingNormalBean)
        {
            holder.bindNormalViewHolder(settingItem)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        fun bindBooleanViewHolder(settingItem: SettingBean.SettingBooleanBean, position: Int)
        {
            val isChecked = Hawk.get(settingItem.settingKey, settingItem.defaultValue)
            itemView.tv_boolean_setting_name.text = settingItem.settingName
            itemView.iv_boolean_setting_checked.isSelected = isChecked

            itemView.iv_boolean_setting_checked.setOnClickListener {
                if (settingItem.needSave)
                {
                    Hawk.put(settingItem.settingKey, !isChecked)
                    notifyItemChanged(position)
                }
                settingItem.callback.invoke(!isChecked)
            }
        }

        fun bindNormalViewHolder(settingItem: SettingBean.SettingNormalBean)
        {
            itemView.tv_normal_setting_name.text = settingItem.settingName

            itemView.setOnClickListener { settingItem.callback.invoke() }
        }
    }
}