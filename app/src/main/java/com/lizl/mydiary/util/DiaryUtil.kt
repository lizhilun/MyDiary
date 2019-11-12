package com.lizl.mydiary.util

import com.lizl.mydiary.R

class DiaryUtil
{
    private val moodResMap = hashMapOf(
        AppConstant.MOOD_HAPPY to R.mipmap.ic_mood_happy,
        AppConstant.MOOD_NORMAL to R.mipmap.ic_mood_normal,
        AppConstant.MOOD_UNHAPPY to R.mipmap.ic_mood_unhappy
    )

    private object Singleton
    {
        val INSTANCE = DiaryUtil()
    }

    companion object
    {
        val instance = Singleton.INSTANCE
    }

    fun getMoodResList(): List<Int> = moodResMap.values.toList()

    fun getMoodResByMood(mood: Int): Int = moodResMap[mood] ?: R.mipmap.ic_mood_normal

    fun getMoodByMoodRes(modRes: Int): Int
    {
        moodResMap.forEach { (key, value) -> if (value == modRes) return key }
        return AppConstant.MOOD_NORMAL
    }
}