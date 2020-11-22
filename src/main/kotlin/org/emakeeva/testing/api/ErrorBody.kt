package org.emakeeva.testing.api

import com.google.gson.annotations.SerializedName

data class ErrorBody(
        @SerializedName("timestamp") val timestamp: String,
        @SerializedName("status") val status: Int,
        @SerializedName("error") var error: String,
        @SerializedName("message") var message: String,
        @SerializedName("path") var path: String
)