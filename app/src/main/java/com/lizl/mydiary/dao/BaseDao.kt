package com.lizl.mydiary.dao

import androidx.room.*

@Dao interface BaseDao<T>
{
    @Insert(onConflict = OnConflictStrategy.REPLACE) fun insert(element: T)

    @Delete fun delete(element: T)

    @Update fun update(element: T)

    @Delete fun deleteList(elements: MutableList<T>)
}