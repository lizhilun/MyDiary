package com.lizl.mydiary.util

import com.lizl.mydiary.bean.HotWordBean
import jackmego.com.jieba_android.JiebaSegmenter

class HotWordUtil
{
    companion object
    {
        fun getHotWordList(): List<HotWordBean>
        {
            val wordMap = HashMap<String, HotWordBean>()
            val diaryList = AppDatabase.instance.getDiaryDao().getAllDiary()
            val content = StringBuffer()
            diaryList.forEach { content.append(it.content) }

            val result = JiebaSegmenter.getJiebaSegmenterSingleton().getDividedString(content.toString())

            val hotWordList = mutableListOf<HotWordBean>()

            result.forEach {
                var hotWordBean = wordMap[it]
                if (hotWordBean == null)
                {
                    hotWordBean = HotWordBean(it, 0)
                    wordMap[it] = hotWordBean
                    hotWordList.add(hotWordBean)
                }

                hotWordBean.freq = hotWordBean.freq + 1
            }

            return hotWordList
        }
    }
}