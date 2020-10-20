package org.emakeeva.testing.api

import com.google.gson.annotations.SerializedName

data class GraphBody(
        @SerializedName("numberOfVertices")
        val numberOfVertices: Int,
        @SerializedName("fromRange")
        val fromRange: Double,
        @SerializedName("toRange")
        val toRange: Double
)