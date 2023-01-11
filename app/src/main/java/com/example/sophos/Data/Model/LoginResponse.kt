package com.example.sophos.Data.Model

import com.google.gson.annotations.SerializedName

data class LoginResponse (
    @SerializedName("id") val id : String,
    @SerializedName("nombre") val uname : String,
    @SerializedName("apellido") val ulastname : String,
    @SerializedName("acceso") val uaccess : Boolean,
    @SerializedName("admin") val uadmin : Boolean
)
