package com.lizl.mydiary.config

import com.blankj.utilcode.util.SPUtils

class BackupConfig
{
    private object Singleton
    {
        val singleton = BackupConfig()
    }

    companion object
    {
        val instance = Singleton.singleton
    }

    fun isAutoBackup() = SPUtils.getInstance().getBoolean(ConfigConstant.IS_AUTO_BACKUP_ON, ConfigConstant.DEFAULT_IS_AUTO_BACKUP_ON)

    fun setAutoBackup(isOn: Boolean) = SPUtils.getInstance().put(ConfigConstant.IS_AUTO_BACKUP_ON, isOn)

    fun setLastAutoBackupTime(time: Long) = SPUtils.getInstance().put(ConfigConstant.APP_LAST_AUTO_BACKUP_TIME, time)

    fun getLastAutoBackupTime() = SPUtils.getInstance().getLong(ConfigConstant.APP_LAST_AUTO_BACKUP_TIME, ConfigConstant.DEFAULT_APP_LAST_AUTO_BACKUP_TIME)

    fun getAppAutoBackupInterval() = SPUtils.getInstance().getLong(ConfigConstant.APP_AUTO_BACKUP_PERIOD, ConfigConstant.DEFAULT_APP_AUTO_BACKUP_PERIOD)

    fun isBackupFileEncryption() = SPUtils.getInstance().getBoolean(ConfigConstant.IS_BACKUP_FILE_ENCRYPTION, ConfigConstant.DEFAULT_IS_BACKUP_FILE_ENCRYPTION)
}