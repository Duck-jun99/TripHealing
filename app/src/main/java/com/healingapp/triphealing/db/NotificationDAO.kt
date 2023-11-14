package com.healingapp.triphealing.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NotificationDAO {

    @Query("SELECT * FROM table_notification")
    fun getAll(): List<NotificationEntity>

    // 확인했는지 여부 확인용 isChecked 가져옴
    @Query("SELECT isChecked FROM table_notification WHERE id = :id")
    fun getNotification(id: Int) : Boolean

    @Query("UPDATE table_notification SET isChecked = 1 WHERE id = :id")
    fun checkNotification(id:Int)

    // 알림 저장 - 중복 값 충돌 발생 시 새로 들어온 데이터로 교체.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveNotification(notificationEntity: NotificationEntity)

    // 알림 삭제
    @Delete
    fun deleteNotification(notificationEntity: NotificationEntity)
}