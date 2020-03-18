package com.lizl.mydiary.adapter

import android.view.View
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.mydiary.R
import com.lizl.mydiary.bean.SettingBean
import com.lizl.mydiary.util.DialogUtil
import kotlinx.android.synthetic.main.item_setting_boolean.view.*
import kotlinx.android.synthetic.main.item_setting_normal.view.*
import kotlinx.android.synthetic.main.item_setting_value.view.*

class SettingListAdapter : BaseDelegateMultiAdapter<SettingBean.SettingBaseBean, SettingListAdapter.ViewHolder>()
{

    companion object
    {
        private const val ITEM_TYPE_DIVIDE = 1
        private const val ITEM_TYPE_BOOLEAN = 2
        private const val ITEM_TYPE_NORMAL = 3
        private const val ITEM_TYPE_INT_RADIO = 4
        private const val ITEM_TYPE_LONG_RADIO = 5
    }

    init
    {
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<SettingBean.SettingBaseBean>()
        {
            override fun getItemType(data: List<SettingBean.SettingBaseBean>, position: Int): Int
            {
                return when (data[position])
                {
                    is SettingBean.SettingDivideBean    -> ITEM_TYPE_DIVIDE
                    is SettingBean.SettingBooleanBean   -> ITEM_TYPE_BOOLEAN
                    is SettingBean.SettingNormalBean    -> ITEM_TYPE_NORMAL
                    is SettingBean.SettingIntRadioBean  -> ITEM_TYPE_INT_RADIO
                    is SettingBean.SettingLongRadioBean -> ITEM_TYPE_LONG_RADIO
                    else                                -> ITEM_TYPE_DIVIDE
                }
            }
        })

        getMultiTypeDelegate()?.let {
            it.addItemType(ITEM_TYPE_DIVIDE, R.layout.item_setting_divide)
            it.addItemType(ITEM_TYPE_BOOLEAN, R.layout.item_setting_boolean)
            it.addItemType(ITEM_TYPE_NORMAL, R.layout.item_setting_normal)
            it.addItemType(ITEM_TYPE_INT_RADIO, R.layout.item_setting_value)
            it.addItemType(ITEM_TYPE_LONG_RADIO, R.layout.item_setting_value)
        }
    }

    override fun convert(helper: ViewHolder, item: SettingBean.SettingBaseBean)
    {
        when (item)
        {
            is SettingBean.SettingBooleanBean   -> helper.bindBooleanViewHolder(item)
            is SettingBean.SettingNormalBean    -> helper.bindNormalViewHolder(item)
            is SettingBean.SettingIntRadioBean  -> helper.bindRadioViewHolder(item)
            is SettingBean.SettingLongRadioBean -> helper.bindRadioViewHolder(item)
        }
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView)
    {
        fun bindBooleanViewHolder(settingItem: SettingBean.SettingBooleanBean)
        {
            val isChecked = settingItem.getValue()
            itemView.tv_boolean_setting_name.text = settingItem.settingName
            itemView.iv_boolean_setting_checked.isSelected = isChecked

            itemView.setOnClickListener {
                if (settingItem.needSave)
                {
                    settingItem.saveValue(!isChecked)
                    update(settingItem)
                }
                settingItem.callback?.invoke(!isChecked, settingItem)
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
                            setData(data.indexOf(settingItem), settingItem)
                            if (settingItem is SettingBean.SettingIntRadioBean)
                            {
                                settingItem.saveValue(it.key as Int)
                                settingItem.callback?.invoke(settingItem)
                            }
                            else if (settingItem is SettingBean.SettingLongRadioBean)
                            {
                                settingItem.saveValue(it.key as Long)
                                settingItem.callback?.invoke(settingItem)
                            }
                            return@showRadioGroupDialog
                        }
                    }
                }
            }
        }
    }

    fun update(item: SettingBean.SettingBaseBean)
    {
        setData(getItemPosition(item), item)
    }
}