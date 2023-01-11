package com.example.sophos.Data.Model

import com.google.gson.annotations.SerializedName

data class SendDocsData(
    @SerializedName("TipoId") var idType : String,
    @SerializedName("Identificacion") var idNum : String,
    @SerializedName("Nombre") var name : String,
    @SerializedName("Apellido") var lastName : String,
    @SerializedName("Ciudad") var city : String,
    @SerializedName("Correo") var email : String,
    @SerializedName("TipoAdjunto") var attachedType : String,
    @SerializedName("Adjunto") var attached : String
)
