package com.example.sophos.Data.Model

import com.google.gson.annotations.SerializedName

data class OfficesItems(
    @SerializedName("IdOficina") val officeId : Int,
    @SerializedName("Nombre") val officeName : String,
    @SerializedName("Ciudad") val officeCity : String,
    @SerializedName("Longitud") val officeLongit : String,
    @SerializedName("Latitud") var officeLatit : String
)
