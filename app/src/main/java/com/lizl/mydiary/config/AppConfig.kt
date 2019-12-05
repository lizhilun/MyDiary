package com.lizl.mydiary.config

object AppConfig
{
    fun getBackupConfig() = BackupConfig.instance

    fun getGeneralConfig() = GeneralConfig.instance

    fun getLayoutStyleConfig() = LayoutStyleConfig.instance

    fun getSecurityConfig() = SecurityConfig.instance
}