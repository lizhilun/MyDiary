package com.lizl.mydiary.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diaries") class DiaryBean : BaseDiaryBean()
{
    @PrimaryKey(autoGenerate = true) var id = 0

    @ColumnInfo var createTime = 0L

    @ColumnInfo var content = ""
}