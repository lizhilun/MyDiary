package com.lizl.mydiary.util

import android.app.Activity
import android.app.Application
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.BarUtils
import com.lizl.mydiary.R
import com.lizl.mydiary.UiApplication
import skin.support.SkinCompatManager
import skin.support.app.SkinCardViewInflater
import skin.support.constraint.app.SkinConstraintViewInflater
import skin.support.design.app.SkinMaterialViewInflater

class SkinUtil
{
    private object Singleton
    {
        val INSTANCE = SkinUtil()
    }

    companion object
    {
        val instance = Singleton.INSTANCE
    }

    fun init(application: Application)
    {
        SkinCompatManager.withoutActivity(application)                         // 基础控件换肤初始化
            .addInflater(SkinMaterialViewInflater())            // material design 控件换肤初始化[可选]
            .addInflater(SkinConstraintViewInflater())          // ConstraintLayout 控件换肤初始化[可选]
            .addInflater(SkinCardViewInflater())                // CardView v7 控件换肤初始化[可选]
            .setSkinWindowBackgroundEnable(true)                   // 关闭windowBackground换肤，默认打开[可选]
    }

    fun loadSkin(activity: Activity)
    {
        if (UiApplication.appConfig.isNightModeOn())
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
        SkinCompatManager.getInstance().loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN)
        BarUtils.setStatusBarColor(activity, ContextCompat.getColor(activity, R.color.colorPrimary_night))
    }

    fun getGlobalTextColor(): Int
    {
        return if (UiApplication.appConfig.isNightModeOn())
        {
            ContextCompat.getColor(UiApplication.instance, R.color.colorTextColor_night)
        }
        else
        {
            ContextCompat.getColor(UiApplication.instance, R.color.colorTextColor)
        }
    }
}