package org.emakeeva.testing.api

import com.google.gson.annotations.SerializedName

data class Graph(
        @SerializedName("numberOfVertices") val numberOfVertices: Int, // количество вершин графа
        @SerializedName("adjacencyMatrix") var adjacencyMatrix: List<List<Double>> // диапазон весов
)