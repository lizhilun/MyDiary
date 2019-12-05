package com.lizl.mydiary.config

import com.blankj.utilcode.util.SPUtils

class GeneralConfig
{
    private object Singleton
    {
        val singleton = GeneralConfig()
    }

    companion object
    {
        val instance = Singleton.singleton
    }

    fun isNightModeOn() = SPUtils.getInstance().getBoolean(ConfigConstant.IS_NIGHT_MODE_ON, ConfigConstant.DEFAULT_NIGHT_MODE_ON)
}