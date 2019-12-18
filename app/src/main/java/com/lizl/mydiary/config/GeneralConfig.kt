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

    fun getAppNightMode() = SPUtils.getInstance().getInt(ConfigConstant.APP_NIGHT_MODE, ConfigConstant.DEFAULT_APP_NIGHT_MODE)
}