package org.emakeeva.testing.workers

import org.emakeeva.testing.api.Graph
import org.emakeeva.testing.api.GraphApiService
import org.emakeeva.testing.api.GraphBody

class GraphWorker {
    var service = GraphApiService()

    fun createGraph(numberOfVertices: Int, fromRange: Double, toRange: Double): Boolean {
        val response = service.createGraph(GraphBody(numberOfVertices, fromRange, toRange)).execute()
        return response.body() == "created"
    }

    fun getGraph(): Graph? {
        val response = service.getGraph().execute()
        return response.body()
    }
}