package com.devyoussef.islamicapptask.util

import com.devyoussef.islamicapptask.model.AzanModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WebServices {

    @GET("calendarByCity/{year}/{month}")
    suspend fun getAzanDates(
        @Path("year") year: Int,
        @Path("month") month: Int,
        @Query("city") city: String,
        @Query("country") country: String,
        @Query("method") method: Int,
    ): AzanModel
}