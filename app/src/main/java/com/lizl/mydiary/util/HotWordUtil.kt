package com.lizl.mydiary.util

import com.lizl.mydiary.UiApplication
import com.lizl.mydiary.bean.CountStatisticsBean
import jackmego.com.jieba_android.JiebaSegmenter

class HotWordUtil
{
    companion object
    {
        fun getHotWordList(count: Int): List<CountStatisticsBean.HotWordStatisticsBean>
        {
            val hotWordList = getHotWordList().sortedByDescending { it.count }
            return if (hotWordList.size > count) hotWordList.subList(0, count) else hotWordList
        }

        private fun getHotWordList(): List<CountStatisticsBean.HotWordStatisticsBean>
        {
            val wordMap = HashMap<String, CountStatisticsBean.HotWordStatisticsBean>()
            val diaryList = AppDatabase.instance.getDiaryDao().getAllDiary()
            val content = StringBuffer()
            diaryList.forEach { content.append(it.content) }

            val result = JiebaSegmenter.getJiebaSegmenterSingleton().getDividedString(content.toString())

            val hotWordList = mutableListOf<CountStatisticsBean.HotWordStatisticsBean>()

            val ignoreWordList = UiApplication.appConfig.getHotWordIgnoreList()

            result.forEach {
                if (it.length < 2 || ignoreWordList.contains(it)) return@forEach
                var hotWordBean = wordMap[it]
                if (hotWordBean == null)
                {
                    hotWordBean = CountStatisticsBean.HotWordStatisticsBean(it, 0)
                    wordMap[it] = hotWordBean
                    hotWordList.add(hotWordBean)
                }

                hotWordBean.count = hotWordBean.count + 1
            }

            return hotWordList
        }

        fun ignoreWord(word: String)
        {
            val ignoreWordList = mutableSetOf<String>()
            ignoreWordList.addAll(UiApplication.appConfig.getHotWordIgnoreList())
            if (!ignoreWordList.contains(word))
            {
                ignoreWordList.add(word)
                UiApplication.appConfig.setHotWordIgnoreList(ignoreWordList)
            }
        }
    }
}