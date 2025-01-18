package com.vc.weatherapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vc.weatherapp.api.Constant
import com.vc.weatherapp.api.NetworkResponse
import com.vc.weatherapp.api.RetrofitInstance
import com.vc.weatherapp.api.WeatherModel
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val weatherApi = RetrofitInstance.weatherApi
    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult : LiveData<NetworkResponse<WeatherModel>> = _weatherResult

    fun getData(city : String){
       // Log.d("City Name : ", city)
        _weatherResult.value = NetworkResponse.Loading

        try {
            viewModelScope.launch {
                val response =  weatherApi.getWeather(Constant.apiKey,city)
                if (response.isSuccessful){
                    response.body().let {
                        _weatherResult.value = NetworkResponse.Success(it!!)
                    }
                    //  Log.d("Response", response.body().toString())
                }else{
                    Log.d("Error", response.message())
                    _weatherResult.value = NetworkResponse.Error("Failed to load data!")
                }
            }
        }catch (e: Exception){
            _weatherResult.value = NetworkResponse.Error("Failed to load data!")
        }



    }


}