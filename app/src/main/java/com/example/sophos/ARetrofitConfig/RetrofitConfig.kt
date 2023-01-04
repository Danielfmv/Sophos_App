package com.example.sophos.ARetrofitConfig

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitConfig {
    companion object {
        var URL_API = "https://6w33tkx4f9.execute-api.us-east-1.amazonaws.com/%E2%80%9D/"
        fun getRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(URL_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}