package com.example.sophos.Data.Network.API

import com.example.sophos.Data.Model.LoginResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LoginAPIClient {
    @GET("/RS_Usuarios")
    suspend fun getLoginByUserandPass (
        @Query("idUsuario") userId : String,
        @Query("clave") userPass : String
    ) : Response<LoginResponse>
}