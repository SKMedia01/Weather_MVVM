package com.example.weather


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.extentions.format
import com.example.weather.storage.ForeCastDatabase
import com.example.weather.ui.adapters.DailyForeCastAdapter
import com.example.weather.ui.adapters.HourlyForeCastAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import com.example.weather.models.Constants
import com.example.weather.models.ForeCast
import com.example.weather.ui.adapters.MainViewModel
import com.google.firebase.messaging.FirebaseMessaging
import org.koin.android.viewmodel.ext.android.getViewModel
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    lateinit var tv_temperature: TextView
    lateinit var tv_date: TextView
    lateinit var tv_feels_like: TextView
    lateinit var tv_temp_max: TextView
    lateinit var tv_temp_min: TextView
    lateinit var tv_weather: TextView
    lateinit var tv_sunrise: TextView
    lateinit var tv_sunset: TextView
    lateinit var tv_humidity: TextView
    lateinit var iv_weather_icon: ImageView
    lateinit var tv_refresh: TextView
    lateinit var progress: ProgressBar
    lateinit var rv_daily_forecast: RecyclerView
    lateinit var rv_hourly_forecast: RecyclerView


    private lateinit var vm: MainViewModel
    private lateinit var dailyForeCastAdapter: DailyForeCastAdapter
    private lateinit var hourlyForeCastAdapter: HourlyForeCastAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_temperature = findViewById(R.id.tv_temperature)
        tv_date = findViewById(R.id.tv_date)
        tv_temp_max =findViewById(R.id.tv_temp_max)
        tv_temp_min =findViewById(R.id.tv_temp_min)
        tv_weather =findViewById(R.id.tv_weather)
        tv_sunrise =findViewById(R.id.tv_sunrise)
        tv_sunset =findViewById(R.id.tv_sunset)
        tv_feels_like =findViewById(R.id.tv_feels_like)
        tv_humidity =findViewById(R.id.tv_humidity)
        iv_weather_icon = findViewById(R.id.iv_weather_icon)
        tv_refresh = findViewById(R.id.tv_refresh)
        progress = findViewById(R.id.progress)
        rv_daily_forecast = findViewById(R.id.rv_daily_forecast)
        rv_hourly_forecast = findViewById(R.id.rv_hourly_forecast)


        vm = getViewModel(MainViewModel::class)
        setupViews()
        setupRecyclerViews()
        subscribeToLiveData()

            FirebaseMessaging.getInstance().token.addOnSuccessListener {
            Log.i("TOKEN", it)
        }

    }

    private fun setupViews() {
        tv_refresh.setOnClickListener{
            vm.showLoading()
            vm.getWeatherFromApi()
        }
    }

    private fun setupRecyclerViews() {
        dailyForeCastAdapter = DailyForeCastAdapter()
        rv_daily_forecast.adapter = dailyForeCastAdapter

        hourlyForeCastAdapter = HourlyForeCastAdapter()
        rv_hourly_forecast.adapter = hourlyForeCastAdapter
    }

    private fun subscribeToLiveData() {
        vm.getForeCastAsLive().observe(this, Observer {
            it?.let {
                setValuesToViews(it)
                loadWeatherIcon(it)
                setDataToRecyclerViews(it)
            }
        })
        vm._isLoading.observe(this, Observer {
            when(it){
                true -> showLoading()
                false -> hideLoading()
            }
        })
    }

    private fun setDataToRecyclerViews(it: ForeCast) {
        it.daily?.let { dailyList ->
            dailyForeCastAdapter.setItemsDaily(dailyList)
        }

        it.hourly?.let { hourlyList ->
            hourlyForeCastAdapter.setItemsHourly(hourlyList)
        }
    }

    private fun showLoading() {
        progress.post {
            progress.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        progress.postDelayed({
            progress.visibility = View.INVISIBLE
        }, 2000)
    }

    private fun setValuesToViews(it: ForeCast) {
        tv_temperature.text = it.current?.temp?.roundToInt().toString()
        tv_date.text = it.current?.date?.format()
        tv_temp_max.text = it.daily?.get(0)?.temp?.max?.roundToInt()?.toString()
        tv_temp_min.text = it.daily?.get(0)?.temp?.min?.roundToInt()?.toString()
        tv_feels_like.text = it.current?.feels_like?.roundToInt()?.toString()
        tv_weather.text = it.current?.weather?.get(0)?.description
        tv_sunrise.text = it.current?.sunrise.format("HH:mm")
        tv_sunset.text = it.current?.sunset.format("HH:mm")
        tv_humidity.text = "${it.current?.humidity?.toString()} %"
    }

    private fun loadWeatherIcon(it: ForeCast) {
        it.current?.weather?.get(0)?.icon?.let { icon ->
            Glide.with(this)
                .load("${Constants.iconUri}${icon}${Constants.iconFormat}")
                .into(iv_weather_icon)
        }
    }
}


