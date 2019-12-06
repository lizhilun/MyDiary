package com.lizl.mydiary.dao

import androidx.room.Dao
import androidx.room.Query
import com.lizl.mydiary.bean.DiaryBean

@Dao
interface DiaryDao : BaseDao<DiaryBean>
{
    @Query("select * from diaries order by createTime desc")
    fun getAllDiary(): MutableList<DiaryBean>

    @Query("select count (*) from diaries")
    fun getDiaryCount(): Int

    @Query("select * from diaries where content like '%' || :keyWord || '%'")
    fun searchDiary(keyWord: String): MutableList<DiaryBean>

    @Query("select * from diaries where tag = :keyWord")
    fun searchDiaryByTag(keyWord: String): MutableList<DiaryBean>

    @Query("select * from diaries where uid == :uid")
    fun getDiaryByUid(uid: String): DiaryBean?

    @Query("DELETE FROM diaries")
    fun deleteAll()
}