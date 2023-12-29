package com.devyoussef.islamicapptask.model.state

import com.devyoussef.islamicapptask.data.local.PrayerEntity
import com.devyoussef.islamicapptask.model.AzanModel

data class StateModel(
    val isLoading: Boolean = false,
    val success: String? = null,
    val error: String? = null,
    val status :String? = null,
    val prayer :List<PrayerEntity>? = null,
)
