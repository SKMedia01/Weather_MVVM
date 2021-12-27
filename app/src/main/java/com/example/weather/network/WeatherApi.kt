package com.example.weather.network

import com.example.weather.models.ForeCast
import io.reactivex.Single

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("onecall")
    fun fetchWeather(
        @Query("lat") lat: Double = 42.8746,
        @Query("lon") lon: Double = 74.5698,
        @Query("exclude") exclude: String = "minutely",
        @Query("appid") appid: String = "92fd06d4bd25395363fb601e3765b529",
        @Query("lang") lang: String = "ru",
        @Query("units") units: String = "metric"

    ): Single<ForeCast>
}