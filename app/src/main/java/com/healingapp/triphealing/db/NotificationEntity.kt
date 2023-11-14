package com.healingapp.triphealing.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_notification")
data class NotificationEntity(

    val title:String,
    val body:String,
    val isChecked:Int,
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}