package com.lizl.mydiary.util

import com.lizl.mydiary.R

class DiaryUtil
{

    companion object
    {
        private val moodResMap = hashMapOf(
            AppConstant.MOOD_HAPPY to R.mipmap.ic_mood_happy,
            AppConstant.MOOD_NORMAL to R.mipmap.ic_mood_normal,
            AppConstant.MOOD_UNHAPPY to R.mipmap.ic_mood_unhappy
        )

        /**
         * 统计字数
         */
        fun sumStringWord(str: String): Int
        {
            return if (str.isEmpty()) 0
            else str.replace("\n", "").replace(" ", "").length
        }

        fun getMoodResList(): List<Int> = moodResMap.values.toList()

        fun getMoodResByMood(mood: Int): Int = moodResMap[mood] ?: R.mipmap.ic_mood_normal

        fun getMoodByMoodRes(modRes: Int): Int
        {
            moodResMap.forEach { (key, value) -> if (value == modRes) return key }
            return AppConstant.MOOD_NORMAL
        }
    }

}