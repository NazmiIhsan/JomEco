package com.example.jomeco.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.jomeco.rewards.BadgeDao
import com.example.jomeco.rewards.UserBadgeDao
import com.example.jomeco.rewards.UserBadgeCrossRef


@Database(entities = [Event::class, User::class, EventRegistration::class
                     ,BadgeEntity::class, UserBadgeCrossRef::class], version = 12, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun userDao(): UserDao
    abstract fun eventRegistrationDao(): EventRegistrationDao
    abstract fun badgeDao(): BadgeDao
    abstract fun userBadgeDao(): UserBadgeDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "jomeco_database"
                ).fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}

