package com.devyoussef.islamicapptask.ui

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.devyoussef.islamicapptask.R
import com.devyoussef.islamicapptask.constant.Constants
import com.devyoussef.islamicapptask.constant.Constants.convertToAmPm
import com.devyoussef.islamicapptask.databinding.ActivityMainBinding
import com.devyoussef.islamicapptask.util.AlarmReceiver
import com.devyoussef.islamicapptask.util.AlarmSchedule
import com.devyoussef.islamicapptask.util.AlarmSchedule.stopAlarm
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private var prayerTimes: List<String?> = emptyList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotificationChannel()
        callApi()

        binding.swipeRefreshLayout.setOnRefreshListener {
            callApi()
            binding.swipeRefreshLayout.isRefreshing = false
        }
        collectStates()
        binding.btnStartTimer.setOnClickListener {
            scheduleAlarms()
        }
        binding.btnStopTimer.setOnClickListener {
            stopAlarm(applicationContext)
            Toast.makeText(applicationContext, "alarm stopped", Toast.LENGTH_SHORT).show()
        }


    }

    private fun callApi() {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance().get(Calendar.MONTH) + 1

        viewModel.getPrayers(
            year = year,
            month = month,
            city = "Alexandria",
            method = 2,
            country = "Egypt"
        )
    }

    private fun collectStates() {
        lifecycleScope.launch {
            viewModel.state.collect {
                when {
                    it.isLoading -> {
                        binding.swipeRefreshLayout.isRefreshing = true
                    }

                    it.error != null -> {
                        binding.swipeRefreshLayout.isRefreshing = false
                        Toast.makeText(applicationContext, it.error.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }

                    it.status == "OK" -> {
                        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - 1
                        binding.swipeRefreshLayout.isRefreshing = false

                        it.prayer?.filter { prayer ->
                            prayer.prayerDate == convertMillisToDateString(System.currentTimeMillis())
                        }?.forEach { prayer ->
                            binding.prayerFajrTime.text =
                                prayer.fajrTime?.get("fajr")?.convertToAmPm() ?: ""
                            binding.prayerDhuhrTime.text =
                                prayer.dhuhrTime?.get("dhuhr")?.convertToAmPm() ?: ""
                            binding.prayerAsrTime.text =
                                prayer.asrTime?.get("asr")?.convertToAmPm() ?: ""
                            binding.prayerMaghribTime.text =
                                prayer.maghribTime?.get("maghrib")?.convertToAmPm() ?: ""
                            binding.prayerIshaTime.text =
                                prayer.ishaTime?.get("isha")?.convertToAmPm() ?: ""

                            binding.txtTimeToday.text = prayer.prayerDate

                            val prayerData = prayer
                            prayerTimes = generatePrayerTimesList(
                                prayerData.prayerDate,
                                prayerData.fajrTime?.get("fajr"),
                                prayerData.dhuhrTime?.get("dhuhr"),
                                prayerData.asrTime?.get("asr"),
                                prayerData.maghribTime?.get("maghrib"),
                                prayerData.ishaTime?.get("isha")
                            )
                        }

                    }
                }
            }
        }
    }

    private fun convertMillisToDateString(millis: Long): String {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val date = Date(millis)
        return sdf.format(date)
    }

    fun getSevenDays(): List<String> {
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
        val currentDate = LocalDate.now()
        val sevenDaysLater = currentDate.plusDays(7)

        val dateList = mutableListOf<String>()

        var currentDateIterator = currentDate
        while (currentDateIterator.isBefore(sevenDaysLater) || currentDateIterator.isEqual(
                sevenDaysLater
            )
        ) {
            dateList.add(currentDateIterator.format(formatter))
            currentDateIterator = currentDateIterator.plusDays(1)
        }

        return dateList
    }

    private fun generatePrayerTimesList(
        readableDate: String?,
        fajr: String?,
        dhuhr: String?,
        asr: String?,
        maghrib: String?,
        isha: String?
    ): List<String> {
        return listOf(
            "$readableDate $fajr",
            "$readableDate $dhuhr",
            "$readableDate $asr",
            "$readableDate $maghrib",
            "$readableDate $isha"
        )
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleAlarm(timeString: String?, prayerName: String) {
        AlarmSchedule.scheduleAlarm(
            context = applicationContext,
            timeString = timeString,
            prayerName = prayerName
        )
    }

    private fun scheduleAlarms() {
        if (prayerTimes.isNotEmpty()) {
            val prayers = listOf("Fajr", "Dhuhr", "Asr", "Maghrib", "Isha")
            prayerTimes.forEachIndexed { index, time ->
                scheduleAlarm(time, prayers[index])
            }
        } else {
            Toast.makeText(
                applicationContext,
                "Prayer times are not available.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            Constants.CHANNELED,
            "Notification Channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }
}