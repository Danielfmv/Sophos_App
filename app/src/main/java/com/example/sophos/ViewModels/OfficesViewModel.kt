package com.example.sophos.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sophos.ARetrofitConfig.RetrofitConfig
import com.example.sophos.Data.Model.OfficesItems
import com.example.sophos.Data.Model.OfficesResponse
import com.example.sophos.Data.Network.API.OfficesAPIClient
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.create

class OfficesViewModel: ViewModel() {

    init {
        getOffices()
    }

    val officesResponseLiveData = MutableLiveData<List<OfficesItems>>()

    fun getOffices () {
        viewModelScope.launch {
            val response: Response<OfficesResponse> = RetrofitConfig.getRetrofit().create(OfficesAPIClient::class.java).getOfficesByCity()
            val citiesInfo = response.body()
            officesResponseLiveData.postValue(citiesInfo?.Items)
            //viewModelScope.cancel()
        }
    }
}