package com.lizl.mydiary.dao

import androidx.room.Dao
import androidx.room.Query
import com.lizl.mydiary.bean.DiaryBean

@Dao
interface DiaryDao : BaseDao<DiaryBean>
{
    @Query("select * from diaries") fun getAllDiary(): MutableList<DiaryBean>

    @Query("select count (*) from diaries") fun getDiaryCount() : Int
}