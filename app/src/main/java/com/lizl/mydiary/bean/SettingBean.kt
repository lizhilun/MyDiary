package com.lizl.mydiary.bean

import com.blankj.utilcode.util.SPUtils

class SettingBean
{
    open class SettingBaseBean

    class SettingDivideBean : SettingBaseBean()

    class SettingNormalBean(val settingName: String, val callback: () -> Unit) : SettingBaseBean()

    class SettingBooleanBean(val settingName: String, val settingKey: String, var defaultValue: Boolean, val needSave: Boolean,
                             val callback: (result: Boolean, bean: SettingBooleanBean) -> Unit) : SettingBaseBean()
    {
        fun getValue() = SPUtils.getInstance().getBoolean(settingKey, defaultValue)

        fun saveValue(value: Boolean) = SPUtils.getInstance().put(settingKey, value)
    }

    class SettingIntRadioBean(val settingName: String, val settingKey: String, val defaultValue: Int, val radioMap: Map<Int, String>,
                              val callback: (bean: SettingIntRadioBean) -> Unit) : SettingBaseBean()
    {
        fun getValue() = SPUtils.getInstance().getInt(settingKey, defaultValue)

        fun saveValue(value: Int) = SPUtils.getInstance().put(settingKey, value)
    }

    class SettingLongRadioBean(val settingName: String, val settingKey: String, val defaultValue: Long, val radioMap: Map<Long, String>,
                               val callback: (bean: SettingLongRadioBean) -> Unit) : SettingBaseBean()
    {
        fun getValue() = SPUtils.getInstance().getLong(settingKey, defaultValue)

        fun saveValue(value: Long) = SPUtils.getInstance().put(settingKey, value)
    }
}