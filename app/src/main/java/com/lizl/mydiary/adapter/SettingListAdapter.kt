package com.lizl.mydiary.adapter

import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.SPUtils
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
        when (viewType)
        {
            ITEM_TYPE_BOOLEAN    -> return ViewHolder(inflateView(R.layout.item_setting_boolean, parent))
            ITEM_TYPE_DIVIDE     -> return ViewHolder(inflateView(R.layout.item_setting_divide, parent))
            ITEM_TYPE_NORMAL     -> return ViewHolder(inflateView(R.layout.item_setting_normal, parent))
            ITEM_TYPE_INT_RADIO  -> return ViewHolder(inflateView(R.layout.item_setting_value, parent))
            ITEM_TYPE_LONG_RADIO -> return ViewHolder(inflateView(R.layout.item_setting_value, parent))
        }
        return ViewHolder(inflateView(R.layout.item_setting_divide, parent))
    }

    override fun bindCustomViewHolder(holder: ViewHolder, bean: SettingBean.SettingBaseBean, position: Int)
    {
        when (bean)
        {
            is SettingBean.SettingBooleanBean   -> holder.bindBooleanViewHolder(bean, position)
            is SettingBean.SettingNormalBean    -> holder.bindNormalViewHolder(bean)
            is SettingBean.SettingIntRadioBean  -> holder.bindIntValueViewHolder(bean)
            is SettingBean.SettingLongRadioBean -> holder.bindValueLongViewHolder(bean)
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
            else                                -> -1
        }
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView)
    {
        fun bindBooleanViewHolder(settingItem: SettingBean.SettingBooleanBean, position: Int)
        {
            val isChecked = SPUtils.getInstance().getBoolean(settingItem.settingKey, settingItem.defaultValue)
            itemView.tv_boolean_setting_name.text = settingItem.settingName
            itemView.iv_boolean_setting_checked.isSelected = isChecked

            itemView.setOnClickListener {
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

        fun bindIntValueViewHolder(settingItem: SettingBean.SettingIntRadioBean)
        {
            itemView.tv_value_setting_name.text = settingItem.settingName

            val settingValue = SPUtils.getInstance().getInt(settingItem.settingKey, settingItem.defaultValue)
            val showValue = settingItem.radioMap.getValue(settingValue)

            itemView.tv_value_setting_value.text = showValue

            val radioList = mutableListOf<String>()
            settingItem.radioMap.forEach { radioList.add(it.value) }

            itemView.setOnClickListener {
                DialogUtil.showRadioGroupDialog(context, settingItem.settingName, radioList, showValue) { result ->
                    settingItem.radioMap.forEach {
                        if (it.value == result)
                        {
                            SPUtils.getInstance().put(settingItem.settingKey, it.key)
                            update(settingItem)
                            settingItem.callback.invoke(settingItem)
                            return@forEach
                        }
                    }
                }
            }
        }

        fun bindValueLongViewHolder(settingItem: SettingBean.SettingLongRadioBean)
        {
            itemView.tv_value_setting_name.text = settingItem.settingName

            val settingValue = SPUtils.getInstance().getLong(settingItem.settingKey, settingItem.defaultValue)
            val showValue = settingItem.radioMap.getValue(settingValue)

            itemView.tv_value_setting_value.text = showValue

            val radioList = mutableListOf<String>()
            settingItem.radioMap.forEach { radioList.add(it.value) }

            itemView.setOnClickListener {
                DialogUtil.showRadioGroupDialog(context, settingItem.settingName, radioList, showValue) { result ->
                    settingItem.radioMap.forEach {
                        if (it.value == result)
                        {
                            SPUtils.getInstance().put(settingItem.settingKey, it.key)
                            update(settingItem)
                            settingItem.callback.invoke(settingItem)
                            return@forEach
                        }
                    }
                }
            }
        }
    }
}