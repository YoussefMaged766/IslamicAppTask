package com.devyoussef.islamicapptask.model.mapper

import com.devyoussef.islamicapptask.constant.Constants.toMap
import com.devyoussef.islamicapptask.data.local.PrayerEntity
import com.devyoussef.islamicapptask.model.AzanModel

fun AzanModel.toPrayerEntity(): List<PrayerEntity> {
    return this.data!!.map {
        PrayerEntity(
            prayerDate = it.date?.readable,
            fajrTime = it.timings?.Fajr?.toMap("fajr"),
            dhuhrTime = it.timings?.Dhuhr?.toMap("dhuhr"),
            asrTime = it.timings?.Asr?.toMap("asr"),
            maghribTime = it.timings?.Maghrib?.toMap("maghrib"),
            ishaTime = it.timings?.Isha?.toMap("isha")
        )
    }

}


