package com.devyoussef.islamicapptask.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PrayerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrayer(prayerEntity:List<PrayerEntity>)

    @Query("SELECT * FROM prayer WHERE prayerDate = :date")
    fun getAllPrayers(date:String):List<PrayerEntity>

    @Query("SELECT COUNT(*) FROM prayer WHERE prayerDate = :date")
    suspend fun checkPrayerExist(date: String): Boolean

    @Update
    suspend fun updatePrayer(prayerEntity: PrayerEntity)
}