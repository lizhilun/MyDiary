package com.lizl.mydiary.util

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.lizl.mydiary.UiApplication
import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.dao.DiaryDao

@Database(entities = [DiaryBean::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase()
{
    private object Singleton
    {
        val singleton: AppDatabase = Room.databaseBuilder(UiApplication.instance, AppDatabase::class.java, "Diary.db")
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .allowMainThreadQueries()
                .build()
    }

    companion object
    {
        val MIGRATION_1_2 = object : Migration(1, 2)
        {
            override fun migrate(database: SupportSQLiteDatabase)
            {
                database.execSQL("ALTER TABLE diaries ADD COLUMN mood INTEGER  NOT NULL DEFAULT 2")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3)
        {
            override fun migrate(database: SupportSQLiteDatabase)
            {
                database.execSQL("ALTER TABLE diaries ADD COLUMN tag TEXT")
            }
        }

        val instance = Singleton.singleton
    }

    abstract fun getDiaryDao(): DiaryDao
}