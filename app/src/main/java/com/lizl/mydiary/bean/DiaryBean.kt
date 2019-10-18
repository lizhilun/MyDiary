package com.lizl.mydiary.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.lizl.mydiary.bean.converter.StringToObjectConverter
import java.io.Serializable

@Entity(tableName = "diaries")
@TypeConverters(StringToObjectConverter::class)
class DiaryBean : Serializable
{
    @PrimaryKey(autoGenerate = true)
    var id = 0

    @ColumnInfo
    var createTime = 0L

    @ColumnInfo
    var content = ""

    @ColumnInfo
    var imageList: List<String>? = null
}