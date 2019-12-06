package com.lizl.mydiary.util

import android.graphics.Bitmap
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.SPUtils
import com.lizl.mydiary.R
import com.lizl.mydiary.UiApplication
import com.lizl.mydiary.config.AppConfig

object DiaryUtil
{

    private val moodResMap = hashMapOf(AppConstant.MOOD_ALL to R.drawable.ic_mood_all, AppConstant.MOOD_HAPPY to R.drawable.ic_mood_happy,
            AppConstant.MOOD_NORMAL to R.drawable.ic_mood_normal, AppConstant.MOOD_UNHAPPY to R.drawable.ic_mood_unhappy)

    private const val DIARY_TAG_LIST = "DIARY_TAG_LIST"

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

    fun getDiaryTagList(): List<String>
    {
        val diaryTagSet = SPUtils.getInstance().getStringSet(DIARY_TAG_LIST, emptySet()).toMutableSet()
        if (diaryTagSet.isEmpty())
        {
            diaryTagSet.add(UiApplication.instance.getString(R.string.diary))
            SPUtils.getInstance().put(DIARY_TAG_LIST, diaryTagSet)
        }
        return diaryTagSet.toList()
    }

    fun addDiaryTag(tag: String)
    {
        if (tag.isBlank()) return
        val diaryTagSet = SPUtils.getInstance().getStringSet(DIARY_TAG_LIST, emptySet()).toMutableSet()
        if (!diaryTagSet.contains(tag))
        {
            diaryTagSet.add(tag)
        }
        SPUtils.getInstance().put(DIARY_TAG_LIST, diaryTagSet)
    }

}