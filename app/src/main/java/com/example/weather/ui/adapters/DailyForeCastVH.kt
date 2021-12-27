package com.example.weather.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.R
import com.example.weather.extentions.format
import com.example.weather.models.Constants
import com.example.weather.models.DailyForeCast
import kotlin.math.roundToInt

class DailyForeCastVH(itemView: View): RecyclerView.ViewHolder(itemView) {
    lateinit var tv_weekday: TextView
    lateinit var tv_precipitation: TextView
    lateinit var tv_temp_max: TextView
    lateinit var tv_min_temp: TextView
    lateinit var iv_weather_icon: ImageView

    fun bind(item: DailyForeCast){
        itemView.run {
            tv_weekday = findViewById(R.id.tv_weekday)
            tv_weekday.text = item.date?.format("dd/MM")

            tv_precipitation = findViewById(R.id.tv_precipitation)
            item.probability?.let {
                tv_precipitation.text = "${(it * 100).roundToInt()}%"
            }

            tv_temp_max = findViewById(R.id.tv_temp_max)
            tv_temp_max.text = item.temp?.max?.roundToInt()?.toString()

            tv_min_temp = findViewById(R.id.tv_min_temp)
            tv_min_temp.text = item.temp?.min?.roundToInt()?.toString()

            iv_weather_icon = findViewById(R.id.iv_weather_icon)
            Glide.with(itemView.context)
                .load("${Constants.iconUri}${item.weather?.get(0)?.icon}${Constants.iconFormat}")
                .into(iv_weather_icon)
        }
    }

    companion object{
        fun create(parent: ViewGroup) : DailyForeCastVH {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_daily_forecast, parent, false)
            return DailyForeCastVH(view)
        }
    }
}