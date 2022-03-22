package com.example.top_up_weather.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.top_up_weather.R
import com.example.top_up_weather.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*


@AndroidEntryPoint
class HomeFragment : Fragment() {

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
        viewModel.getWeather.observe(viewLifecycleOwner, Observer { uiEvent ->
            uiEvent.getContentIfNotHandled()?.let {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        progress.visibility = View.VISIBLE
                    }
                    Resource.Status.SUCCESS -> {
                        progress.visibility = View.GONE
                        textView.text = it.data?.list.toString()
                        Log.d("response", it.data?.list.toString())
                    }
                    Resource.Status.ERROR -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                }
            }

        })
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}