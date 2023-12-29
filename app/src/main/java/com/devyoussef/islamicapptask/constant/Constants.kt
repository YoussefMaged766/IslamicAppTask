package com.devyoussef.islamicapptask.constant

import java.text.SimpleDateFormat
import java.util.Locale

object Constants {
    const val BASEURL = "https://api.aladhan.com/v1/"
    const val CHANNELED = "CHANNELED"
    const val NOTIFICATION_ID = 100
    fun String.convertToAmPm(): String {
        val inputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val outputFormat = SimpleDateFormat("h:mm a", Locale.getDefault())

        try {
            val parsedTime = inputFormat.parse(this)
            return outputFormat.format(parsedTime!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return this
    }

    fun String.toMap(key: String): Map<String, String> {
        val map = mutableMapOf<String, String>()
        map[key] = this
        return map
    }
}