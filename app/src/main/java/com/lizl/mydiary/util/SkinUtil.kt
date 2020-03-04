package com.lizl.mydiary.util

import android.app.Activity
import android.app.Application
import android.content.res.Configuration
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.BarUtils
import com.lizl.mydiary.R
import com.lizl.mydiary.UiApplication
import com.lizl.mydiary.config.AppConfig
import com.lizl.mydiary.config.ConfigConstant
import skin.support.SkinCompatManager
import skin.support.app.SkinCardViewInflater
import skin.support.constraint.app.SkinConstraintViewInflater
import skin.support.design.app.SkinMaterialViewInflater

object SkinUtil
{

    private val SKIN_NIGHT = "night"

    fun init(application: Application)
    {
        SkinCompatManager.withoutActivity(application)                         // 基础控件换肤初始化
            .addInflater(SkinMaterialViewInflater())            // material design 控件换肤初始化[可选]
            .addInflater(SkinConstraintViewInflater())          // ConstraintLayout 控件换肤初始化[可选]
            .addInflater(SkinCardViewInflater())                // CardView v7 控件换肤初始化[可选]
            .setSkinWindowBackgroundEnable(false)
    }

    fun loadSkin(activity: Activity)
    {
        if (isNightModeOn())
        {
            loadNightSkin(activity)
        }
        else
        {
            loadDefaultSkin(activity)
        }
    }

    private fun loadDefaultSkin(activity: Activity)
    {
        SkinCompatManager.getInstance().restoreDefaultTheme()
        BarUtils.setStatusBarColor(activity, ContextCompat.getColor(activity, R.color.colorPrimary))
    }

    private fun loadNightSkin(activity: Activity)
    {
        SkinCompatManager.getInstance().loadSkin(SKIN_NIGHT, SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN)
        BarUtils.setStatusBarColor(activity, ContextCompat.getColor(activity, R.color.colorPrimary_night))
    }

    fun getGlobalTextColor(): Int
    {
        return if (isNightModeOn())
        {
            ContextCompat.getColor(UiApplication.instance, R.color.colorTextColor_night)
        }
        else
        {
            ContextCompat.getColor(UiApplication.instance, R.color.colorTextColor)
        }
    }

    private fun isNightModeOn(): Boolean
    {
        return when (AppConfig.getGeneralConfig().getAppNightMode())
        {
            ConfigConstant.APP_NIGHT_MODE_ON            -> true
            ConfigConstant.APP_NIGHT_MODE_OFF           -> false
            ConfigConstant.APP_NIGHT_MODE_FOLLOW_SYSTEM -> isSystemDarkMode()
            else                                        -> false
        }
    }

    private fun isSystemDarkMode(): Boolean
    {
        return UiApplication.instance.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }

}