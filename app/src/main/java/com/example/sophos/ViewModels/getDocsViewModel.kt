package com.example.sophos.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sophos.ARetrofitConfig.RetrofitConfig
import com.example.sophos.Data.Model.LoginResponse
import com.example.sophos.Data.Model.SendDocsData
import com.example.sophos.Data.Model.getDocsItems
import com.example.sophos.Data.Model.getDocsResponse
import com.example.sophos.Data.Network.API.GetDocsAPI
import com.example.sophos.Data.Services.servicesGetDocs
import com.example.sophos.Utils.contenedorDatos
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class getDocsViewModel : ViewModel() {

    private val api = RetrofitConfig.getRetrofit().create(GetDocsAPI::class.java)

    private var _getDocsLiveData = MutableLiveData<getDocsResponse?>()
    val getDocsLiveData : LiveData<getDocsResponse?>
        get() = _getDocsLiveData


    fun getDocs(email: String) {

        val servicesDocs = servicesGetDocs()

        servicesDocs.getDocsByEmail(email).enqueue(object : Callback<getDocsResponse> {
            override fun onResponse(
                call: Call<getDocsResponse>,
                response: Response<getDocsResponse>
            ) {
                if (response.isSuccessful){
                    println(response.body())
                    _getDocsLiveData.value = response.body()
                }
            }
            override fun onFailure(call: Call<getDocsResponse>, t: Throwable) {
                Log.e("Error", t.message.toString())
                call.cancel()
            }
        })
    }



}