package com.lizl.mydiary.config


class ConfigConstant
{
    companion object
    {
        // 指纹解锁是否开启
        const val IS_FINGERPRINT_LOCK_ON = "IS_FINGERPRINT_LOCK_ON"
        const val DEFAULT_IS_FINGERPRINT_LOCK_ON = false

        // 密码保护是否开启
        const val IS_APP_LOCK_ON = "IS_APP_LOCK_ON"
        const val DEFAULT_IS_APP_LOCK_ON = false

        // APP保护密码
        const val APP_LOCK_PASSWORD = "APP_LOCK_PASSWORD"
        const val DEFAULT_APP_LOCK_PASSWORD = ""

        // APP上次停止的时间（用于判断是否达到超时锁定时长）
        const val APP_LAST_STOP_TIME = "APP_LAST_STOP_TIME"
        const val DEFAULT_APP_LAST_STOP_TIME: Long = 0

        // APP超时锁定时长（默认为5分钟）
        const val APP_TIMEOUT_PERIOD = 5 * 60 * 1000

        // 图片保存质量
        const val IMAGE_SAVE_QUALITY_LOW = 30
        const val IMAGE_SAVE_QUALITY_MEDIUM = 60
        const val IMAGE_SAVE_QUALITY_ORIGINAL = 100
        const val IMAGE_SAVE_QUALITY = "IMAGE_SAVE_QUALITY"
        const val DEFAULT_IMAGE_SAVE_QUALITY = IMAGE_SAVE_QUALITY_ORIGINAL

        // 夜间模式
        const val IS_NIGHT_MODE_ON = "IS_NIGHT_MODE_ON"
        const val DEFAULT_NIGHT_MODE_ON = false

        // 自动备份
        const val IS_AUTO_BACKUP_ON = "IS_AUTO_BACKUP_ON"
        const val DEFAULT_IS_AUTO_BACKUP_ON = false

        // 上次自动备份时间
        const val APP_LAST_AUTO_BACKUP_TIME = "APP_LAST_AUTO_BACKUP_TIME"
        const val DEFAULT_APP_LAST_AUTO_BACKUP_TIME: Long = 0
        // 自动备份间隔（一天）
        const val APP_AUTO_BACKUP_PERIOD = 24 * 60 * 60 * 1000
    }
}