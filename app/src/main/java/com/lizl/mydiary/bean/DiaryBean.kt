package com.lizl.mydiary.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.lizl.mydiary.bean.converter.StringToObjectConverter
import com.lizl.mydiary.util.AppConstant
import java.io.Serializable

@Entity(tableName = "diaries")
@TypeConverters(StringToObjectConverter::class)
class DiaryBean : Serializable
{
    @PrimaryKey
    var uid = ""

    @ColumnInfo
    var createTime = 0L

    @ColumnInfo
    var content = ""

    @ColumnInfo
    var mood = AppConstant.MOOD_NORMAL

    @ColumnInfo
    var tag: String? = null

    @ColumnInfo
    var imageList: List<String>? = null
}
