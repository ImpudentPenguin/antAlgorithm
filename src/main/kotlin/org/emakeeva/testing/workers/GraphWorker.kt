package org.emakeeva.testing.workers

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.emakeeva.testing.api.ErrorBody
import org.emakeeva.testing.api.Graph
import org.emakeeva.testing.api.GraphApiService
import org.emakeeva.testing.api.GraphBody
import org.emakeeva.testing.utils.*

class GraphWorker {
    var service = GraphApiService()

    fun createGraph(numberOfVertices: Int, fromRange: Double, toRange: Double): Boolean {
        val response = service.createGraph(GraphBody(numberOfVertices, fromRange, toRange)).execute()
        when {
            response.isSuccessful -> {
                return response.body() == "created"
            }
            !response.isSuccessful -> {
                val gson = Gson()
                val type = object : TypeToken<ErrorBody>() {}.type
                val error: ErrorBody? = gson.fromJson(response.errorBody()?.charStream(), type)

                when (error?.message) {
                    "Graph hasn't vertices" -> {
                        throw GraphHasntVerticesException()
                    }
                    "Graph has one vertex" -> {
                        throw GraphHasOneVertexException()
                    }
                    "Distance: going beyond boundaries" -> {
                        if (fromRange < 0 || toRange < 0)
                            throw GraphNegativeDistanceException()
                        else
                            throw GraphIncorrectDistanceException()
                    }
                    "Limit vertices of the graph" -> {
                        throw GraphLimitVerticesException()
                    }
                    else -> {
                        throw Exception("Oops. Something went wrong")
                    }
                }
            }
            else -> {
                return response.body() == "created"
            }
        }
    }

    fun getGraph(): Graph? {
        val response = service.getGraph().execute()
        return response.body()
    }
}