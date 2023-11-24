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

    // id 가져옴
    @Query("SELECT id FROM table_notification")
    fun getId(): Int

    // 확인했는지 여부 확인용 isChecked 가져옴
    @Query("SELECT isChecked FROM table_notification WHERE id = :id")
    fun getNotification(id: Int) : Int

    @Query("UPDATE table_notification SET isChecked = 1 WHERE id = :id")
    fun checkNotification(id: Int)

    @Query("DELETE From table_notification WHERE id = :id")
    fun deleteNotification(id: Int)

    // 알림 저장 - 중복 값 충돌 발생 시 새로 들어온 데이터로 교체.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveNotification(notificationEntity: NotificationEntity)

    // 알림 삭제
    @Delete
    fun deleteNotification(notificationEntity: NotificationEntity)
}