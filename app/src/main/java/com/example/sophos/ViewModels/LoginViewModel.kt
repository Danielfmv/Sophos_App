package com.example.sophos.ViewModels

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sophos.ARetrofitConfig.RetrofitConfig
import com.example.sophos.Data.Model.LoginResponse
import com.example.sophos.Data.Network.API.LoginAPIClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel: ViewModel() {

    private val _logInResponse : MutableLiveData<Response<LoginAPIClient>> = MutableLiveData()
    val apiLoginResponse : LiveData<Response<LoginAPIClient>>
        get() = _logInResponse

    fun searchByUser (email: String, password: String) {

    }

    // LOGIN CON DATOS DE USUARIO //

    fun login (email: String, password: String, activity: AppCompatActivity) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitConfig.getRetrofit().create(LoginAPIClient::class.java).getLoginByUserandPass(email, password)
        }
    }
}