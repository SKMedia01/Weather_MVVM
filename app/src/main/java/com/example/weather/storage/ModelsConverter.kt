package com.example.weather.storage

import androidx.room.TypeConverter
import com.example.weather.models.CurrentForeCast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ModelsConverter {

    @TypeConverter
    fun fromCurrentForeCastToJson (forecast: CurrentForeCast?) : String? =
        Gson().toJson(forecast)

    @TypeConverter
    fun fromJsonToCurrentForeCast (json: String?) : CurrentForeCast? =
        Gson().fromJson(json, object : TypeToken<CurrentForeCast>() {}.type)
}