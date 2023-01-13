package com.example.sophos.Utils

import com.example.sophos.Data.Model.LoginResponse
import com.example.sophos.ViewModels.LoginViewModel

class contenedorDatos {

    fun userEmail () : String {
        val userEmail = LoginViewModel().userEmail
        return userEmail
    }

    fun userName () : String {
        val userName = LoginViewModel().userName
        return userName
    }

}