package com.healingapp.triphealing.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ScheduleDAO {

    @Query("SELECT * FROM table_schedule")
    fun getAll(): List<ScheduleEntity>

}