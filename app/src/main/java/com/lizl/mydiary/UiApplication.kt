package com.lizl.mydiary

import android.app.Application
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import com.lizl.mydiary.config.AppConfig
import com.lizl.mydiary.util.AppConstant
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
        val appConfig: AppConfig by lazy { AppConfig.instance }
    }

    override fun onCreate()
    {
        super.onCreate()

        Hawk.init(this).build()

        checkFingerprintStatus()
    }

    /**
     * 检查指纹识别状态
     */
    private fun checkFingerprintStatus()
    {
        // 只有在未检测过情况下才检测
        if (appConfig.getAppFingerprintStatus() != AppConstant.APP_FINGERPRINT_STATUS_NOT_DETECT)
        {
            return
        }

        try
        {
            val mFingerprintManager = FingerprintManagerCompat.from(instance)
            if (mFingerprintManager.isHardwareDetected)
            {
                appConfig.setAppFingerprintStatus(AppConstant.APP_FINGERPRINT_STATUS_SUPPORT)
            }
            else
            {
                appConfig.setAppFingerprintStatus(AppConstant.APP_FINGERPRINT_STATUS_NOT_SUPPORT)
            }
        }
        catch (e: ClassNotFoundException)
        {
            appConfig.setAppFingerprintStatus(AppConstant.APP_FINGERPRINT_STATUS_NOT_SUPPORT)
        }
    }
}