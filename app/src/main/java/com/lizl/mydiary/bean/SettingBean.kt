package com.lizl.mydiary.bean

import com.blankj.utilcode.util.SPUtils

class SettingBean
{
    open class SettingBaseBean

    class SettingDivideBean : SettingBaseBean()

    class SettingNormalBean(val settingName: String, val callback: () -> Unit) : SettingBaseBean()

    class SettingBooleanBean(val settingName: String, val settingKey: String, var defaultValue: Boolean, val needSave: Boolean,
                             val callback: ((result: Boolean, bean: SettingBooleanBean) -> Unit)? = null) : SettingBaseBean()
    {
        fun getValue() = SPUtils.getInstance().getBoolean(settingKey, defaultValue)

        fun saveValue(value: Boolean) = SPUtils.getInstance().put(settingKey, value)
    }

    abstract class SettingRadioBean<T, T2>(open val settingName: String, open val settingKey: String, open val defaultValue: T,
                                           open val radioMap: Map<T, String>, open val callback: ((bean: T2) -> Unit)? = null) : SettingBaseBean()
    {
        abstract fun getValue(): T

        abstract fun saveValue(value: T)
    }

    class SettingIntRadioBean(override val settingName: String, override val settingKey: String, override val defaultValue: Int,
                              override val radioMap: Map<Int, String>, override val callback: ((bean: SettingIntRadioBean) -> Unit)? = null) :
            SettingRadioBean<Int, SettingIntRadioBean>(settingName, settingKey, defaultValue, radioMap, callback)
    {
        override fun getValue(): Int = SPUtils.getInstance().getInt(settingKey, defaultValue)

        override fun saveValue(value: Int) = SPUtils.getInstance().put(settingKey, value)
    }

    class SettingLongRadioBean(override val settingName: String, override val settingKey: String, override val defaultValue: Long,
                               override val radioMap: Map<Long, String>, override val callback: ((bean: SettingLongRadioBean) -> Unit)? = null) :
            SettingRadioBean<Long, SettingLongRadioBean>(settingName, settingKey, defaultValue, radioMap, callback)
    {
        override fun getValue() = SPUtils.getInstance().getLong(settingKey, defaultValue)

        override fun saveValue(value: Long) = SPUtils.getInstance().put(settingKey, value)
    }
}