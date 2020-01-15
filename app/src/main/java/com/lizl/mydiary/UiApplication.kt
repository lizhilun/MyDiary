package com.lizl.mydiary

import android.app.Application
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import com.blankj.utilcode.util.Utils
import com.lizl.mydiary.config.AppConfig
import com.lizl.mydiary.mvp.activity.DiaryContentActivity
import com.lizl.mydiary.util.BackupUtil
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
    }

    override fun onCreate()
    {
        super.onCreate()

        Utils.init(this)
        SkinUtil.init(this)

        setupShortcuts()

        val autoBackupPeriod = AppConfig.getBackupConfig().getAppAutoBackupInterval()
        if (AppConfig.getBackupConfig().isAutoBackup() && autoBackupPeriod > 0
            && System.currentTimeMillis() - AppConfig.getBackupConfig().getLastAutoBackupTime() > autoBackupPeriod)
        {
            BackupUtil.autoBackup()
        }
    }

    private fun setupShortcuts()
    {
        val shortcutManager = getSystemService(ShortcutManager::class.java)
        val shortcutInfoList = mutableListOf<ShortcutInfo>()

        val intent = Intent(this, DiaryContentActivity::class.java)
        intent.action = Intent.ACTION_VIEW
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val shortcutInfo = ShortcutInfo.Builder(this, "new add").setShortLabel(getString(R.string.new_add)).setLongLabel(getString(R.string.new_add))
            .setIcon(Icon.createWithResource(this, R.drawable.ic_add_round)).setIntent(intent).build()

        shortcutInfoList.add(shortcutInfo)

        shortcutManager.dynamicShortcuts = shortcutInfoList
    }
}