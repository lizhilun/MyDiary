package com.lizl.mydiary.util

import android.app.Activity
import android.content.Intent
import com.blankj.utilcode.util.ActivityUtils
import java.io.Serializable

object ActivityUtil
{
    fun turnToActivity(cls: Class<out Activity>, vararg extraList: Any)
    {
        ActivityUtils.finishActivity(cls)
        val topActivity = ActivityUtils.getTopActivity() ?: return
        val intent = Intent(topActivity, cls)

        extraList.forEach {
            when (it)
            {
                is Int          -> intent.putExtra(AppConstant.BUNDLE_DATA_INT, it)
                is String       -> intent.putExtra(AppConstant.BUNDLE_DATA_STRING, it)
                is Boolean      -> intent.putExtra(AppConstant.BUNDLE_DATA_BOOLEAN, it)
                is ArrayList<*> -> intent.putExtra(AppConstant.BUNDLE_DATA_STRING_ARRAY, it)
                is Serializable -> intent.putExtra(AppConstant.BUNDLE_DATA_OBJECT, it)
            }
        }
        topActivity.startActivity(intent)
    }
}