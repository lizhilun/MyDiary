package com.lizl.mydiary

import android.app.Application
import com.blankj.utilcode.util.Utils
import com.lizl.mydiary.config.AppConfig
import com.lizl.mydiary.util.SkinUtil
import kotlin.properties.Delegates


class UiApplication : Application()
{
    init
    {
        instance = this
    }

    companion object
    {
        var instance: UiApplication by Delegates.notNull()
        val appConfig: AppConfig by lazy { AppConfig.instance }
    }

    override fun onCreate()
    {
        super.onCreate()

        Utils.init(this)
        SkinUtil.instance.init(this)

        // Activity走onCreate()将上次应用停止时间置为0，保证onResume()会走是否显示锁定界面流程
        appConfig.setAppLastStopTime(0)
    }
}