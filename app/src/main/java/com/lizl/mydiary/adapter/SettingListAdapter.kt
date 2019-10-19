package com.lizl.mydiary.adapter

import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.SPUtils
import com.lizl.mydiary.R
import com.lizl.mydiary.bean.SettingBean
import kotlinx.android.synthetic.main.item_setting_boolean.view.*
import kotlinx.android.synthetic.main.item_setting_normal.view.*

class SettingListAdapter() : BaseAdapter<SettingBean.SettingBaseBean, SettingListAdapter.ViewHolder>()
{

    companion object
    {
        const val ITEM_TYPE_DIVIDE = 1
        const val ITEM_TYPE_BOOLEAN = 2
        const val ITEM_TYPE_NORMAL = 3
    }

    override fun createCustomViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        when (viewType)
        {
            ITEM_TYPE_BOOLEAN -> return ViewHolder(inflateView(R.layout.item_setting_boolean, parent))
            ITEM_TYPE_DIVIDE  -> return ViewHolder(inflateView(R.layout.item_setting_divide, parent))
            ITEM_TYPE_NORMAL  -> return ViewHolder(inflateView(R.layout.item_setting_normal, parent))
        }
        return ViewHolder(inflateView(R.layout.item_setting_divide, parent))
    }

    override fun bindCustomViewHolder(holder: ViewHolder, bean: SettingBean.SettingBaseBean, position: Int)
    {
        if (bean is SettingBean.SettingBooleanBean)
        {
            holder.bindBooleanViewHolder(bean, position)
        } else if (bean is SettingBean.SettingNormalBean)
        {
            holder.bindNormalViewHolder(bean)
        }
    }

    override fun getCustomItemViewType(position: Int): Int
    {
        return when (getItem(position))
        {
            is SettingBean.SettingDivideBean  -> ITEM_TYPE_DIVIDE
            is SettingBean.SettingBooleanBean -> ITEM_TYPE_BOOLEAN
            is SettingBean.SettingNormalBean  -> ITEM_TYPE_NORMAL
            else                              -> -1
        }
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView)
    {
        fun bindBooleanViewHolder(settingItem: SettingBean.SettingBooleanBean, position: Int)
        {
            val isChecked = SPUtils.getInstance().getBoolean(settingItem.settingKey, settingItem.defaultValue)
            itemView.tv_boolean_setting_name.text = settingItem.settingName
            itemView.iv_boolean_setting_checked.isSelected = isChecked

            itemView.iv_boolean_setting_checked.setOnClickListener {
                if (settingItem.needSave)
                {
                    SPUtils.getInstance().put(settingItem.settingKey, !isChecked)
                    notifyItemChanged(position)
                }
                settingItem.callback.invoke(!isChecked, settingItem)
            }
        }

        fun bindNormalViewHolder(settingItem: SettingBean.SettingNormalBean)
        {
            itemView.tv_normal_setting_name.text = settingItem.settingName

            itemView.setOnClickListener { settingItem.callback.invoke() }
        }
    }
}