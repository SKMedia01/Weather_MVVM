package com.example.weather.repo

import com.example.weather.models.ForeCast
import com.example.weather.network.WeatherApi
import com.example.weather.storage.ForeCastDatabase
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class WeatherRepo(

    private val db: ForeCastDatabase,
    private val weatherApi: WeatherApi
) {
    fun getWeatherFromApi(): Single<ForeCast> {
        return weatherApi.fetchWeather()
            .subscribeOn(Schedulers.io())
            .map {
                db.forecastDao().insert(it)
                it
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getForeCastFromDbAsLive() = db.forecastDao().getAll()
}