package com.lizl.mydiary.config

import com.lizl.mydiary.util.AppConstant
import com.orhanobut.hawk.Hawk

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

    fun isAppLockOn(): Boolean = Hawk.get(ConfigConstant.IS_APP_LOCK_ON, ConfigConstant.DEFAULT_IS_APP_LOCK_ON)

    fun setAppLockOn(isAppLockOn: Boolean) = Hawk.put(ConfigConstant.IS_APP_LOCK_ON, isAppLockOn)

    fun getAppLockPassword(): String = Hawk.get(ConfigConstant.APP_LOCK_PASSWORD, ConfigConstant.DEFAULT_APP_LOCK_PASSWORD)

    fun setAppLockPassword(password: String) = Hawk.put(ConfigConstant.APP_LOCK_PASSWORD, password)

    fun isFingerprintLockOn(): Boolean = Hawk.get(ConfigConstant.IS_FINGERPRINT_LOCK_ON, ConfigConstant.DEFAULT_IS_FINGERPRINT_LOCK_ON)

    fun setAppLastStopTime(stopTime: Long) = Hawk.put(ConfigConstant.APP_LAST_STOP_TIME, stopTime)

    fun getAppLastStopTime(): Long = Hawk.get(ConfigConstant.APP_LAST_STOP_TIME, ConfigConstant.DEFAULT_APP_LAST_STOP_TIME)

}