package com.healingapp.triphealing.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_schedule")
data class ScheduleEntity(

    val title:String,
    val body:String,
    val isChecked:Int,
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}