package com.example.sophos.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sophos.ARetrofitConfig.RetrofitConfig
import com.example.sophos.Data.Model.getDocsItems
import com.example.sophos.Data.Model.getDocsResponse
import com.example.sophos.Data.Network.API.GetDocsAPI
import com.example.sophos.Data.Services.servicesGetDocs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class getDocsViewModel : ViewModel() {

    private val api = RetrofitConfig.getRetrofit().create(GetDocsAPI::class.java)

    private var _getDocsLiveData = MutableLiveData<getDocsResponse?>()
    val getDocsLiveData : LiveData<getDocsResponse?>
        get() = _getDocsLiveData

    var base64String : String = ""
    var sentDate : String = ""

    fun getDocs(email: String) {

        val servicesDocs = servicesGetDocs()

        servicesDocs.getDocsByEmail(email).enqueue(object : Callback<getDocsResponse> {
            override fun onResponse(
                call: Call<getDocsResponse>,
                response: Response<getDocsResponse>
            ) {
                if (response.isSuccessful){
                    //println(response.body())
                    _getDocsLiveData.value = response.body()
                }
            }
            override fun onFailure(call: Call<getDocsResponse>, t: Throwable) {
                Log.e("Error", t.message.toString())
                call.cancel()
            }
        })
    }

    fun getImg(registId : String) {

        val servicesId = servicesGetDocs()

        servicesId.getDocsById(registId).enqueue(object : Callback<getDocsResponse> {
            override fun onResponse(
                call: Call<getDocsResponse>,
                response: Response<getDocsResponse>
            ) {
                if (response.isSuccessful){
                    _getDocsLiveData.value = response.body()
                    base64String = getDocsLiveData.value?.Items?.get(0)?.attached.toString()
                }
            }
            override fun onFailure(call: Call<getDocsResponse>, t: Throwable) {
                Log.e("Error", t.message.toString())
                call.cancel()
            }
        })
    }
}