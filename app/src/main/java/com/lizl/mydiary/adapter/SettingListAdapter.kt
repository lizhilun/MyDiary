package com.lizl.mydiary.adapter

import android.view.View
import android.view.ViewGroup
import com.lizl.mydiary.R
import com.lizl.mydiary.bean.SettingBean
import com.lizl.mydiary.util.DialogUtil
import kotlinx.android.synthetic.main.item_setting_boolean.view.*
import kotlinx.android.synthetic.main.item_setting_normal.view.*
import kotlinx.android.synthetic.main.item_setting_value.view.*

class SettingListAdapter : BaseAdapter<SettingBean.SettingBaseBean, SettingListAdapter.ViewHolder>()
{

    companion object
    {
        const val ITEM_TYPE_DIVIDE = 1
        const val ITEM_TYPE_BOOLEAN = 2
        const val ITEM_TYPE_NORMAL = 3
        const val ITEM_TYPE_INT_RADIO = 4
        const val ITEM_TYPE_LONG_RADIO = 5
    }

    override fun createCustomViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(inflateView(when (viewType)
        {
            ITEM_TYPE_BOOLEAN    -> R.layout.item_setting_boolean
            ITEM_TYPE_DIVIDE     -> R.layout.item_setting_divide
            ITEM_TYPE_NORMAL     -> R.layout.item_setting_normal
            ITEM_TYPE_INT_RADIO  -> R.layout.item_setting_value
            ITEM_TYPE_LONG_RADIO -> R.layout.item_setting_value
            else                 -> R.layout.item_setting_divide
        }, parent))
    }

    override fun bindCustomViewHolder(holder: ViewHolder, bean: SettingBean.SettingBaseBean, position: Int)
    {
        when (bean)
        {
            is SettingBean.SettingBooleanBean   -> holder.bindBooleanViewHolder(bean, position)
            is SettingBean.SettingNormalBean    -> holder.bindNormalViewHolder(bean)
            is SettingBean.SettingIntRadioBean  -> holder.bindRadioViewHolder(bean)
            is SettingBean.SettingLongRadioBean -> holder.bindRadioViewHolder(bean)
        }
    }

    override fun getCustomItemViewType(position: Int): Int
    {
        return when (getItem(position))
        {
            is SettingBean.SettingDivideBean    -> ITEM_TYPE_DIVIDE
            is SettingBean.SettingBooleanBean   -> ITEM_TYPE_BOOLEAN
            is SettingBean.SettingNormalBean    -> ITEM_TYPE_NORMAL
            is SettingBean.SettingIntRadioBean  -> ITEM_TYPE_INT_RADIO
            is SettingBean.SettingLongRadioBean -> ITEM_TYPE_LONG_RADIO
            else                                -> ITEM_TYPE_DIVIDE
        }
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView)
    {
        fun bindBooleanViewHolder(settingItem: SettingBean.SettingBooleanBean, position: Int)
        {
            val isChecked = settingItem.getValue()
            itemView.tv_boolean_setting_name.text = settingItem.settingName
            itemView.iv_boolean_setting_checked.isSelected = isChecked

            itemView.setOnClickListener {
                if (settingItem.needSave)
                {
                    settingItem.saveValue(!isChecked)
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

        fun bindRadioViewHolder(settingItem: SettingBean.SettingRadioBean<*, *>)
        {
            itemView.tv_value_setting_name.text = settingItem.settingName

            val showValue = when (settingItem)
            {
                is SettingBean.SettingIntRadioBean  -> settingItem.radioMap.getValue(settingItem.getValue())
                is SettingBean.SettingLongRadioBean -> settingItem.radioMap.getValue(settingItem.getValue())
                else                                -> ""
            }

            itemView.tv_value_setting_value.text = showValue

            val radioList = mutableListOf<String>()
            settingItem.radioMap.forEach { radioList.add(it.value) }

            itemView.setOnClickListener {
                DialogUtil.showRadioGroupDialog(context, settingItem.settingName, radioList, showValue) { result ->
                    settingItem.radioMap.forEach {
                        if (it.value == result)
                        {
                            update(settingItem)
                            if (settingItem is SettingBean.SettingIntRadioBean)
                            {
                                settingItem.saveValue(it.key as Int)
                                settingItem.callback.invoke(settingItem)
                            }
                            else if (settingItem is SettingBean.SettingLongRadioBean)
                            {
                                settingItem.saveValue(it.key as Long)
                                settingItem.callback.invoke(settingItem)
                            }
                            return@showRadioGroupDialog
                        }
                    }
                }
            }
        }
    }
}