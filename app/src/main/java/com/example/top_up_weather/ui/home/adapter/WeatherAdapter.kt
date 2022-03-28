package com.example.top_up_weather.ui.home.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.top_up_weather.R
import com.example.top_up_weather.data.model.CityWeather
import kotlinx.android.synthetic.main.item_layout.view.*
import java.util.*

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.top_up_weather.R
import com.example.top_up_weather.data.model.CityWeather
import com.example.top_up_weather.data.model.Weather
import kotlinx.android.synthetic.main.item_layout.view.*
import java.util.*


class WeatherAdapter(private var list: MutableList<CityWeather>, private var delegate: OnItemClickListener?) :
    ListAdapter<CityWeather, WeatherAdapter.ItemListViewHolder>(
        DIFF_CALLBACK
    ) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return ItemListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ItemListViewHolder, position: Int) {

        holder.bindView(list[position])
        holder.bindSelectedItem(holder.itemView, delegate, list[position])

    }


    class ItemListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(item: CityWeather) {
            itemView.title.text = item.name
            itemView.temp.text = item.main.temp.toString()
            if (item.isLiked) {
                itemView.unliked.setImageResource(R.drawable.heart_liked)
                Log.e("isLike", "thisonewasliked")
            } else {
                itemView.unliked.setImageResource(R.drawable.heart)
            }
        }

        fun bindSelectedItem(
            position: View,
            delegate: OnItemClickListener?,
            cityWeather: CityWeather
        ) {
//            itemView.liked.setOnClickListener {
//                delegate?.onLikeClicked(position,cityWeather)
//            }
            itemView.unliked.setOnClickListener {
                delegate?.onLikeClicked(position, cityWeather, this.layoutPosition)
            }
            itemView.liked.setOnClickListener {
                delegate?.onUnlikeClicked(position, cityWeather,this.layoutPosition)
            }
            itemView.setOnClickListener {
                delegate?.onItemClicked(position, cityWeather)
            }
        }
    }


    companion object {
        private val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<CityWeather>() {
                override fun areItemsTheSame(
                    oldItem: CityWeather,
                    newItem: CityWeather
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: CityWeather,
                    newItem: CityWeather
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }

    interface OnItemClickListener {
        fun onLikeClicked(view: View, cityWeather: CityWeather,position: Int)
        fun onItemClicked(view: View, cityWeather: CityWeather)
        fun onUnlikeClicked(view: View, cityWeather: CityWeather,position: Int)
    }

    fun swapItem(fromPosition: Int, toPosition: Int) {
        Collections.swap(list, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }
}
