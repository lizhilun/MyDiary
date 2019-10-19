package com.lizl.mydiary.config

import com.blankj.utilcode.util.SPUtils

class AppConfig
{
    private object Singleton
    {
        val singleton = AppConfig()
    }

    companion object
    {
        val instance = Singleton.singleton
    }

    fun isAppLockOn() = SPUtils.getInstance().getBoolean(ConfigConstant.IS_APP_LOCK_ON, ConfigConstant.DEFAULT_IS_APP_LOCK_ON)

    fun setAppLockOn(isAppLockOn: Boolean) = SPUtils.getInstance().put(ConfigConstant.IS_APP_LOCK_ON, isAppLockOn)

    fun getAppLockPassword() = SPUtils.getInstance().getString(ConfigConstant.APP_LOCK_PASSWORD, ConfigConstant.DEFAULT_APP_LOCK_PASSWORD)

    fun setAppLockPassword(password: String) = SPUtils.getInstance().put(ConfigConstant.APP_LOCK_PASSWORD, password)

    fun isFingerprintLockOn() = SPUtils.getInstance().getBoolean(ConfigConstant.IS_FINGERPRINT_LOCK_ON, ConfigConstant.DEFAULT_IS_FINGERPRINT_LOCK_ON)

    fun setAppLastStopTime(stopTime: Long) = SPUtils.getInstance().put(ConfigConstant.APP_LAST_STOP_TIME, stopTime)

    fun getAppLastStopTime() = SPUtils.getInstance().getLong(ConfigConstant.APP_LAST_STOP_TIME, ConfigConstant.DEFAULT_APP_LAST_STOP_TIME)
}