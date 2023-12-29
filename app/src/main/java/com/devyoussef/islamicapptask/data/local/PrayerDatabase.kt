package com.devyoussef.islamicapptask.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [PrayerEntity::class], version = 2, exportSchema = false)
@TypeConverters(Converter::class)
abstract class PrayerDatabase : RoomDatabase(){
    abstract fun prayerDao(): PrayerDao
}