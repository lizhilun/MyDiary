package com.lizl.mydiary.bean

class CountStatisticsBean
{
    open class BaseCountStatisticsBean(open var count: Int)

    class MoodStatisticsBean(val mood: Int, override var count: Int) : BaseCountStatisticsBean(count)

    class HotWordStatisticsBean(val word: String, override var count: Int) : BaseCountStatisticsBean(count)
}