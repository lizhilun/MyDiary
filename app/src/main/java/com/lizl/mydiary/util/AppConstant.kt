package com.lizl.mydiary.util

class AppConstant
{
    companion object
    {
        const val BUNDLE_DATA_OBJECT = "BUNDLE_DATA_OBJECT"
        const val BUNDLE_DATA_STRING_ARRAY = "BUNDLE_DATA_STRING_ARRAY"
        const val BUNDLE_DATA_STRING = "BUNDLE_DATA_STRING"

        const val APP_FINGERPRINT_STATUS_NOT_DETECT = -1 // 指纹识别状态：没有检测设备
        const val APP_FINGERPRINT_STATUS_NOT_SUPPORT = 0 // 指纹识别状态：设备不支持
        const val APP_FINGERPRINT_STATUS_SUPPORT = 1 // 指纹识别状态：设备支持
    }
}