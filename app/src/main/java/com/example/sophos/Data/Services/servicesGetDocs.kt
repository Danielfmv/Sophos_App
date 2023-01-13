package com.example.sophos.Data.Services

import androidx.lifecycle.viewModelScope
import com.example.sophos.ARetrofitConfig.RetrofitConfig
import com.example.sophos.Data.Model.getDocsResponse
import com.example.sophos.Data.Network.API.GetDocsAPI
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class servicesGetDocs {

    private val api = RetrofitConfig.getRetrofit().create(GetDocsAPI::class.java)

    fun getDocsByEmail (email : String) : Call<getDocsResponse> {
        return api.getDocsByEmailApi(email)
    }

    fun getDocsById (id : String) : Call<getDocsResponse> {
        return api.getDocsByIdApi(id)
    }
}