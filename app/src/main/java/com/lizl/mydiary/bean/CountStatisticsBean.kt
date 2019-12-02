package com.lizl.mydiary.bean

class CountStatisticsBean
{
    open class BaseCountStatisticsBean(open var count: Int)

    class MoodStatisticsBean(val mood: Int, override var count: Int) : BaseCountStatisticsBean(count)

    class TimeStatisticsBean(val startTime: Int, override var count: Int) : BaseCountStatisticsBean(count)
}