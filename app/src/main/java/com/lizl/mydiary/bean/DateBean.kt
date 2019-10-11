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
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }
}