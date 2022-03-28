package com.example.top_up_weather.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isGone
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

    var adapter : WeatherAdapter? = null

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

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
                        weather_list.layoutManager?.scrollToPosition(0)
                        adapter = WeatherAdapter(list, this)
                        weather_list.adapter = adapter
                        Log.e("newliiist", list.toString())
                    }
                    Resource.Status.ERROR -> {
                        tv_error2.text = it.message
                        progress.visibility = View.GONE
                    }
                }
            }
        })

        viewModel.weatherCities.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    progress.visibility = View.VISIBLE
                }
                Resource.Status.SUCCESS -> {
                    progress.visibility = View.GONE
                    weather_list.visibility = View.VISIBLE
                    val list = mutableListOf<CityWeather>()
                    it.data?.let { list.addAll(it) }
                    weather_list.layoutManager?.scrollToPosition(0)
                    adapter = WeatherAdapter(list, this)
                    weather_list.adapter = adapter
                    Log.e("newliiist", list.toString())
                }
                Resource.Status.ERROR -> {
                    tv_error2.text = it.message
                    progress.visibility = View.GONE
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu,menu)

        val menuItem = menu.findItem(R.id.action_search)
        val searchView: SearchView = menuItem.actionView as SearchView
        searchView.queryHint = "Type here to search"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.search(newText.trim())
                return false
            }

        })
        super.onCreateOptionsMenu(menu, inflater)


    }

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onLikeClicked(view: View, cityWeather: CityWeather, position: Int) {
        if(!cityWeather.isLiked){
            cityWeather.isLiked
            adapter?.swapItem(position,0)
            val newWeather =  cityWeather.copy(isLiked = true)
            view.unliked.setImageResource(R.drawable.heart_liked)
            Log.e("newlysaved", newWeather.toString())
            viewModel.saveWeather(newWeather)
            viewModel.getWeatherList()
            Snackbar.make(requireView(), "Added city to favourites", Snackbar.LENGTH_LONG).show()
        }
         else{
            val newWeather =  cityWeather.copy(isLiked = false)
            view.unliked.setImageResource(R.drawable.heart)
            Log.e("newlyunliked", newWeather.toString())
            viewModel.saveWeather(newWeather)
            viewModel.getWeatherList()
            Snackbar.make(requireView(), "removed city from favourites", Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onItemClicked(view: View, cityWeather: CityWeather) {
        Navigation.findNavController(view).navigate(HomeFragmentDirections.actionHomeFragmentToWeatherDetailFragment(cityWeather))
    }

    override fun onUnlikeClicked(view: View, cityWeather: CityWeather,position: Int) {
        if(cityWeather.isLiked){
            val newWeather =  cityWeather.copy(isLiked = false)
            view.unliked.setImageResource(R.drawable.heart)
            viewModel.saveWeather(newWeather)
            viewModel.getWeatherList()
            Snackbar.make(requireView(), "removed city from favourites", Snackbar.LENGTH_LONG).show()
        }
        else{
            val newWeather =  cityWeather.copy(isLiked = true)
            view.unliked.setImageResource(R.drawable.heart_liked)
            Log.e("newlyunliked", newWeather.toString())
            adapter?.swapItem(position,0)
            viewModel.saveWeather(newWeather)
            viewModel.getWeatherList()
            Snackbar.make(requireView(), "Added city to favourites", Snackbar.LENGTH_LONG).show()
        }
    }
}