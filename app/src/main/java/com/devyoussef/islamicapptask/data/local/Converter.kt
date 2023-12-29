package com.devyoussef.islamicapptask.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converter {
    private val gson = Gson()

    @TypeConverter
    fun fromString(value: String?): Map<String, String>? {
        if (value == null) {
            return null
        }
        val type = object : TypeToken<Map<String, String>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun toString(value: Map<String, String>?): String? {
        return gson.toJson(value)
    }
}