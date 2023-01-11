package com.example.sophos.Data.Network.API

import com.example.sophos.Data.Model.OfficesItems
import com.example.sophos.Data.Model.OfficesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OfficesAPIClient {
    @GET("/RS_Oficinas")
    suspend fun getOfficesByCity (
    ) : Response<OfficesResponse>
}