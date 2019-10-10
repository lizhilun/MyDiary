package com.lizl.mydiary.util

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lizl.mydiary.UiApplication
import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.dao.DiaryDao

@Database(entities = [DiaryBean::class], version = 1) abstract class AppDatabase : RoomDatabase()
{
    private object Singleton
    {
        val singleton: AppDatabase = Room.databaseBuilder(
            UiApplication.instance, AppDatabase::class.java, "Diary.db"
        ).allowMainThreadQueries().build()
    }

    companion object
    {
        val instance = Singleton.singleton
    }

    abstract fun getDiaryDao(): DiaryDao
}