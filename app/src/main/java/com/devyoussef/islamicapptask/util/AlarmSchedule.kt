package com.devyoussef.islamicapptask.util

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object AlarmSchedule {
    @SuppressLint("ScheduleExactAlarm")
    fun scheduleAlarm(
        context: Context,
        timeString: String?,
        prayerName: String,

    ){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("prayer_name", prayerName)

        val timeInMillis = convertToMillis(timeString)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            (0 .. 1999992).random(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
    }

    private fun convertToMillis(timeString: String?): Long {
        if (timeString.isNullOrBlank()) {
            return 0L
        }

        val dateFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("EET")

        try {
            val date = dateFormat.parse(timeString)
            return date?.time ?: 0L
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return 0L
    }

    fun stopAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            (0 .. 1999992).random(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.cancel(pendingIntent)
    }
}
