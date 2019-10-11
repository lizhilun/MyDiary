package com.lizl.mydiary

import android.app.Application
import com.lizl.mydiary.config.AppConfig
import com.orhanobut.hawk.Hawk
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
        val appConfig: AppConfig by lazy { AppConfig() }
    }

    override fun onCreate()
    {
        super.onCreate()

        Hawk.init(this).build()
    }
}