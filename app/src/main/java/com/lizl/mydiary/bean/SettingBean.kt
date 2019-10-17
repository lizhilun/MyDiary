package com.lizl.mydiary.bean

class SettingBean
{
    open class SettingBaseBean

    class SettingDivideBean : SettingBaseBean()

    class SettingNormalBean(val settingName: String, val callback: () -> Unit) : SettingBaseBean()

    class SettingBooleanBean(val settingName: String, val settingKey: String, var defaultValue: Boolean, val needSave: Boolean,
                             val callback: (result: Boolean, bean: SettingBooleanBean) -> Unit) : SettingBaseBean()
}