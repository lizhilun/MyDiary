package com.lizl.mydiary.util

import android.graphics.Bitmap
import com.blankj.utilcode.util.ImageUtils
import com.lizl.mydiary.R
import com.lizl.mydiary.config.AppConfig

object DiaryUtil
{

    private val moodResMap = hashMapOf(AppConstant.MOOD_ALL to R.drawable.ic_mood_all, AppConstant.MOOD_HAPPY to R.drawable.ic_mood_happy,
            AppConstant.MOOD_NORMAL to R.drawable.ic_mood_normal, AppConstant.MOOD_UNHAPPY to R.drawable.ic_mood_unhappy)

    /**
     * 统计字数
     */
    fun sumStringWord(str: String): Int
    {
        return if (str.isEmpty()) 0
        else str.replace("\n", "").replace(" ", "").length
    }

    /**
     * 保存图片
     */
    fun saveDiaryImage(imagePath: String): String
    {
        val bitmap = ImageUtils.getBitmap(imagePath)
        val comBitmap = ImageUtils.compressByQuality(bitmap, AppConfig.getLayoutStyleConfig().getImageSaveQuality())
        val savePath = "${FileUtil.getImageFileSavePath()}/${System.currentTimeMillis()}.jpg"
        ImageUtils.save(comBitmap, savePath, Bitmap.CompressFormat.JPEG)
        return savePath
    }

    fun getMoodResList(withAll: Boolean): List<Int> = moodResMap.filter { withAll || it.key != AppConstant.MOOD_ALL }.values.toList()

    fun getMoodList(withAll: Boolean): List<Int> = moodResMap.filter { withAll || it.key != AppConstant.MOOD_ALL }.keys.toList()

    fun getMoodResByMood(mood: Int): Int = moodResMap[mood] ?: R.drawable.ic_mood_normal

    fun getMoodByMoodRes(modRes: Int): Int
    {
        moodResMap.forEach { (key, value) -> if (value == modRes) return key }
        return AppConstant.MOOD_NORMAL
    }

}