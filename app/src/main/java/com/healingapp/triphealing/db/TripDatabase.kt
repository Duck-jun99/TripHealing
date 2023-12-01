package com.healingapp.triphealing.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [NotificationEntity::class, ScheduleEntity::class], version = 1)
abstract class TripDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationDAO
    abstract fun scheduleDAO(): ScheduleDAO

    // 데이터 베이스 객체를 싱글톤으로 인스턴스화.
    companion object {
        private var instance: TripDatabase? = null

        @Synchronized
        fun getInstance(context: Context): TripDatabase? {
            if (instance == null)
                synchronized(Database::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TripDatabase::class.java,
                        "database"
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