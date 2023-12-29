package com.devyoussef.islamicapptask.data.remote

import android.content.Context
import com.devyoussef.islamicapptask.data.local.PrayerDao
import com.devyoussef.islamicapptask.model.StatusModel
import com.devyoussef.islamicapptask.model.mapper.toPrayerEntity
import com.devyoussef.islamicapptask.util.NetworkUtils
import com.devyoussef.islamicapptask.util.Status
import com.devyoussef.islamicapptask.util.WebServices
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class Repo @Inject constructor(
    val webServices: WebServices,
    @ApplicationContext private val context: Context,
    private val prayerDao: PrayerDao
) {
    private val gson = Gson()
    suspend fun getAzanDates(year: Int, month: Int, city: String, country: String, method: Int) =
        flow {
            if (NetworkUtils(context).isNetworkConnected()) {
                try {
                    emit(Status.Loading)
                    val response = webServices.getAzanDates(
                        year = year,
                        month = month,
                        city = city,
                        country = country,
                        method = method
                    )
                    emit(Status.Success(response.toPrayerEntity()))
                    prayerDao.insertPrayer(response.toPrayerEntity())

                } catch (e: Throwable) {
                    when (e) {
                        is HttpException -> {
                            val type = object : TypeToken<StatusModel>() {}.type
                            val errorResponse: StatusModel? =
                                gson.fromJson(e.response()?.errorBody()!!.charStream(), type)
                            emit(Status.Error(errorResponse?.data.toString()))
                        }

                        is Exception -> {
                            emit(Status.Error(e.message.toString()))
                        }
                    }
                }
            } else {
                if (prayerDao.checkPrayerExist(getCurrentDateFormatted())) {
                    emit(Status.Success(prayerDao.getAllPrayers(getCurrentDateFormatted())))
                } else {
                    emit(Status.Error("No Internet Connection"))
                }
            }

        }.flowOn(Dispatchers.IO)


    private fun getCurrentDateFormatted(): String {
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val currentDate = Calendar.getInstance().time
        return dateFormat.format(currentDate)
    }
}