package com.example.top_up_weather.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.top_up_weather.R
import com.example.top_up_weather.data.model.CityWeather
import com.example.top_up_weather.data.model.Weather
import com.example.top_up_weather.ui.home.adapter.WeatherAdapter
import com.example.top_up_weather.utils.Resource
import com.google.android.material.snackbar.Snackbar
import com.like.LikeButton
import com.like.OnLikeListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_layout.view.*
import com.like.LikeButton

import com.like.OnLikeListener





@AndroidEntryPoint
class HomeFragment : Fragment(), WeatherAdapter.OnItemClickListener {

    var weather : Weather? = null

    private val viewModel: HomeViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getWeatherList()
        setUpObserver()
        textView.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.weatherDetailFragment)
        }
    }

    private fun setUpObserver() {
        viewModel.getWeather.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {

                when (it.status) {
                    Resource.Status.LOADING -> {
                        progress.visibility = View.VISIBLE
                    }
                    Resource.Status.SUCCESS -> {
                        progress.visibility = View.GONE
                        val list = mutableListOf<CityWeather>()
                        it.data?.let { list.addAll(it) }
                        weather_list.adapter = WeatherAdapter(list, this)
                        Log.e("newliiist", list.toString())
                    }
                    Resource.Status.ERROR -> {
                        tv_error2.text = it.message
                        progress.visibility = View.GONE
                    }
                }
            }
        })
    }

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onLikeClicked(view: View, cityWeather: CityWeather) {
        if(!cityWeather.isLiked){
            cityWeather.isLiked
            val newWeather =  cityWeather.copy(isLiked = true)
            view.unliked.setImageResource(R.drawable.heart_liked)
            Log.e("newlysaved", newWeather.toString())
            viewModel.saveWeather(newWeather)
            Snackbar.make(requireView(), "Added city to favourites", Snackbar.LENGTH_LONG).show()
        }
         else{
            val newWeather =  cityWeather.copy(isLiked = false)
            view.unliked.setImageResource(R.drawable.heart)
            Log.e("newlyunliked", newWeather.toString())
            viewModel.saveWeather(newWeather)
            Snackbar.make(requireView(), "removed city from favourites", Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onItemClicked(view: View, cityWeather: CityWeather) {
        Navigation.findNavController(view).navigate(HomeFragmentDirections.actionHomeFragmentToWeatherDetailFragment(cityWeather))
    }

    override fun onUnlikeClicked(view: View, cityWeather: CityWeather) {
        if(cityWeather.isLiked){
            val newWeather =  cityWeather.copy(isLiked = false)
            view.unliked.setImageResource(R.drawable.heart)
            viewModel.saveWeather(newWeather)
            Snackbar.make(requireView(), "removed city from favourites", Snackbar.LENGTH_LONG).show()
        }
        else{
            val newWeather =  cityWeather.copy(isLiked = true)
            view.unliked.setImageResource(R.drawable.heart_liked)
            Log.e("newlyunliked", newWeather.toString())
            viewModel.saveWeather(newWeather)
            Snackbar.make(requireView(), "Added city to favourites", Snackbar.LENGTH_LONG).show()
        }
    }
}