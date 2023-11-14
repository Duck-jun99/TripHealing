package com.healingapp.triphealing.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [NotificationEntity::class], version = 1)
abstract class NotificationDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationDAO

    // 데이터 베이스 객체를 싱글톤으로 인스턴스화.
    companion object {
        private var instance: NotificationDatabase? = null

        @Synchronized
        fun getInstance(context: Context): NotificationDatabase? {
            if (instance == null)
                synchronized(NotificationDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NotificationDatabase::class.java,
                        "notification-database"
                    )
                        .build()
                }
            return instance
        }

        fun destroyInstance() {
            instance = null
        }
    }
}