package com.example.sophos.Data.Network.API

import com.example.sophos.Data.Model.SendDocsData
import com.example.sophos.Data.Model.SendDocsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SendDocsAPI {
    @POST("/RS_Documentos")
    suspend fun postDocstoApi (
        @Body sendDocsData : SendDocsData
    ) : Response<SendDocsResponse>
}