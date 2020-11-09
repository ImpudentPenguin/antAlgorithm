package org.emakeeva.testing.api

import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GraphApiService {
    private var service: IGraphApiService

    init {
        val gson = GsonBuilder()
                .setLenient()
                .create()

        val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl("https://antbackend.herokuapp.com")
                .build()

        service = retrofit.create<IGraphApiService>(
                IGraphApiService::class.java)
    }

    fun getGraph(): Call<Graph> = service.getGraph()
    fun createGraph(graphBody: GraphBody): Call<String> = service.createGraph(graphBody)
}