package com.lizl.mydiary.bean

import com.lizl.mydiary.R
import com.lizl.mydiary.UiApplication
import java.util.*

class DateBean(time: Long)
{
    companion object
    {
        private val weekList = listOf(
                UiApplication.instance.getString(R.string.sunday), UiApplication.instance.getString(R.string.monday),
                UiApplication.instance.getString(R.string.tuesday), UiApplication.instance.getString(R.string.wednesday),
                UiApplication.instance.getString(R.string.thursday), UiApplication.instance.getString(R.string.friday),
                UiApplication.instance.getString(R.string.saturday)
        )
    }

    var year = 0
        private set
    var month = 0
        private set
    var day = 0
        private set
    var week = ""
        private set
    var hour = 0
        private set
    var minute = 0
        private set
    var second = 0
        private set

    init
    {
        try
        {
            val calendar = Calendar.getInstance()
            calendar.time = Date(time)
            year = calendar.get(Calendar.YEAR)
            month = calendar.get(Calendar.MONTH) + 1
            day = calendar.get(Calendar.DAY_OF_MONTH)
            week = weekList[calendar.get(Calendar.DAY_OF_WEEK) - 1]
            hour = calendar.get(Calendar.HOUR_OF_DAY)
            minute = calendar.get(Calendar.MINUTE)
            second = calendar.get(Calendar.SECOND)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    fun getHourAndMinute(): String
    {
        val stringBuilder = StringBuilder()
        if (hour < 10)
        {
            stringBuilder.append(0)
        }
        stringBuilder.append(hour)

        stringBuilder.append(":")

        if (minute < 10)
        {
            stringBuilder.append(0)
        }
        stringBuilder.append(minute)

        return stringBuilder.toString()
    }
}