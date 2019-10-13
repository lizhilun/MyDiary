package com.lizl.mydiary.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "diaries") class DiaryBean : BaseDiaryBean(), Serializable
{
    @PrimaryKey(autoGenerate = true) var id = 0

    @ColumnInfo var createTime = 0L

    @ColumnInfo var content = ""
}