package com.example.sophos.Data.Network.API

import com.example.sophos.Data.Model.OfficesResponse
import com.example.sophos.Data.Model.getDocsResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GetDocsAPI {
    @GET("/RS_Documentos")
    fun getDocsByEmailApi(
        @Query("correo") email : String
    ): Call<getDocsResponse>

    @GET("/RS_Documentos")
    fun getDocsByIdApi(
        @Query("IdRegistro") userId : String
    ): Call<getDocsResponse>

}
