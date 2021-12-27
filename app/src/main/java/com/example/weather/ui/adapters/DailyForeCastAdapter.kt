package com.example.weather.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.models.DailyForeCast


class DailyForeCastAdapter: RecyclerView.Adapter<DailyForeCastVH>() {
    private val items = arrayListOf<DailyForeCast>()

    fun setItemsDaily(newItems: List<DailyForeCast>){
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyForeCastVH {
        return  DailyForeCastVH.create(parent)
    }

    override fun onBindViewHolder(holder: DailyForeCastVH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.count()
}
