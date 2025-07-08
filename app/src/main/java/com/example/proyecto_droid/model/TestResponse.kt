package com.example.proyecto_droid.model

import com.google.gson.annotations.SerializedName

data class TestResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String
)