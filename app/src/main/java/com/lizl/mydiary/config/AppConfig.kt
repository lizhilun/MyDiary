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

    fun isAppLockOn() = Hawk.get(ConfigConstant.IS_APP_LOCK_ON, ConfigConstant.DEFAULT_IS_APP_LOCK_ON)

    fun setAppLockOn(isAppLockOn: Boolean) = Hawk.put(ConfigConstant.IS_APP_LOCK_ON, isAppLockOn)

    fun getAppFingerprintStatus() = Hawk.get(ConfigConstant.APP_FINGERPRINT_STATUS, AppConstant.APP_FINGERPRINT_STATUS_NOT_DETECT)

    fun isSupportFingerprint() = getAppFingerprintStatus() == AppConstant.APP_FINGERPRINT_STATUS_SUPPORT

    fun setAppFingerprintStatus(status: Int) = Hawk.put(ConfigConstant.APP_FINGERPRINT_STATUS, status)

}