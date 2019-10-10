package com.lizl.mydiary

import android.app.Application
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
    }
}