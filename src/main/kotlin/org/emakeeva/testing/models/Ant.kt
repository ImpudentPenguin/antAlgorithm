package org.emakeeva.testing.models

import org.emakeeva.testing.api.Graph

class Ant() {
    private var graph: Graph? = null
    var tour: MutableList<Int> = mutableListOf()
    var visited: MutableList<Boolean> = mutableListOf()

    constructor(graph: Graph) : this() {
        this.graph = graph
        visited = MutableList(graph.numberOfVertices) { false }
        tour = MutableList(graph.numberOfVertices) { -1 }
    }

    fun visitTown(town: Int, currentIndex: Int) {
        tour[currentIndex + 1] = town
        visited[town] = true
    }

    fun tourLength(size: Int): Double {
        graph?.adjacencyMatrix?.let {
            var length = it[tour[size - 1]][tour[0]]
            for (i in 0 until size - 1)
                length += it[tour[i]][tour[i + 1]]
            return length
        }
        return Double.MAX_VALUE
    }

    fun clear(size: Int) {
        for (i in 0 until size)
            visited[i] = false
    }
}