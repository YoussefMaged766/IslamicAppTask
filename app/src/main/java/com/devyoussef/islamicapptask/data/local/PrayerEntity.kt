package com.devyoussef.islamicapptask.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prayer")
data class PrayerEntity(

    val prayerDate: String? = null,
    val fajrTime: Map<String ,String>? = null,
    val dhuhrTime: Map<String ,String>? = null,
    val asrTime: Map<String ,String>? = null,
    val maghribTime: Map<String ,String>? = null,
    val ishaTime: Map<String ,String>? = null,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)


