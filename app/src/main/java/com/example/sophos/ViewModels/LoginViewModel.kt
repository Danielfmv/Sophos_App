package com.example.sophos.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sophos.ARetrofitConfig.RetrofitConfig
import com.example.sophos.Data.Model.LoginResponse
import com.example.sophos.Data.Network.API.LoginAPIClient
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel: ViewModel() {

    private var _logInResponse: MutableLiveData<Response<LoginResponse>> = MutableLiveData()
    val apiLoginResponse: LiveData<Response<LoginResponse>>
        get() = _logInResponse

    var userEmail : String = ""
    var userName : String = ""
    var userId : String = ""

    // LOGIN CON DATOS DE USUARIO //

    fun login(email: String, password: String) =
        viewModelScope.launch {
            _logInResponse.value = RetrofitConfig.getRetrofit().create(LoginAPIClient::class.java).getLoginByUserandPass(email, password)
            val response : Response<LoginResponse> = RetrofitConfig.getRetrofit().create(LoginAPIClient::class.java).getLoginByUserandPass(email, password)
            val userInfo = _logInResponse.value!!.body()
            println(_logInResponse.value.toString())
            println(userInfo.toString())

            if (response.isSuccessful) {
                if (userInfo!!.uaccess) {
                    userEmail = email
                    userName = userInfo.uname
                    userId = userInfo.id
                } else {
                    println(response.code().toString())
                    println(response)
                    println(userInfo)
                }
            } else {
                println(response.code().toString())
                println(response.errorBody())
            }

        }
}



