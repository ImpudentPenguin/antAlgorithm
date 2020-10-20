package org.emakeeva.testing.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface IGraphApiService {

    @GET("/graph")
    fun getGraph(): Call<Graph>

    @POST("/create")
    fun createGraph(@Body body: GraphBody): Call<String>
}