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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_layout.view.*
import com.like.LikeButton

import com.like.OnLikeListener





@AndroidEntryPoint
class HomeFragment : Fragment(), WeatherAdapter.OnItemClickListener {

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

            val list = mutableListOf<CityWeather>()
            it.data?.list?.let { list.addAll(it) }
           // weather_list.adapter = WeatherAdapter(list,this)
            Log.e("newliiist",list.toString())

            progress.isVisible = it is Resource.Loading && it.data?.list.isNullOrEmpty()
            tv_error2.isVisible= it is Resource.Error && it.data?.list.isNullOrEmpty()
            tv_error2.text = it.message

        })
    }

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onLikeClicked(view: View, cityWeather: CityWeather) {

        view.star_button .setOnLikeListener(object : OnLikeListener {
            override fun liked(likeButton: LikeButton) {
               Toast.makeText(requireContext(),"liiiiked",Toast.LENGTH_LONG).show()
            }
            override fun unLiked(likeButton: LikeButton) {
                Toast.makeText(requireContext(),"unliike",Toast.LENGTH_LONG).show()
            }
        })
    }
}