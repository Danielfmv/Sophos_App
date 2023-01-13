package com.example.sophos.ViewModels

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sophos.ARetrofitConfig.RetrofitConfig
import com.example.sophos.Data.Model.OfficesResponse
import com.example.sophos.Data.Model.SendDocsData
import com.example.sophos.Data.Model.SendDocsResponse
import com.example.sophos.Data.Network.API.OfficesAPIClient
import com.example.sophos.Data.Network.API.SendDocsAPI
import kotlinx.coroutines.launch
import retrofit2.Response

class SendDocsViewModel: ViewModel() {

    init {
        getOfficesCities()
    }

    val sendDocsLiveData = MutableLiveData<SendDocsData?>()
    val officesCitiesLiveData = MutableLiveData<MutableList<String>>()

//    fun sendDocuments (userDocs : SendDocsData)= viewModelScope.launch {
//        RetrofitConfig.getRetrofit().create(SendDocsAPI::class.java).postDocstoApi(userDocs)
//    }

    fun sendDocuments (userDocs : SendDocsData)= viewModelScope.launch {
        userDocs.attached = "data:image/jpg;base64,${userDocs.attached.replace("\n", "")}"
        val responsePost = RetrofitConfig.getRetrofit().create(SendDocsAPI::class.java).postDocstoApi(userDocs)
        println(userDocs)
        println(responsePost.isSuccessful)

        if (responsePost.isSuccessful){
            println(responsePost.body().toString())
        } else {
            println(responsePost.code().toString())
            println(responsePost.errorBody())
        }
     }

    fun getOfficesCities () {
        viewModelScope.launch {
            val response: Response<OfficesResponse> =
                RetrofitConfig.getRetrofit().create(OfficesAPIClient::class.java).getOfficesByCity()
            val cityList = response.body()?.Items

            if (cityList != null) {
                val listOfCities = mutableSetOf<String>()
                for (city in cityList.indices) {
                    listOfCities.add(cityList[city].officeCity)
                }

                officesCitiesLiveData.postValue(listOfCities.toMutableList())

            }
        }
    }
}