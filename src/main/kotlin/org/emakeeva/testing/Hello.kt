package org.emakeeva.testing

import org.emakeeva.testing.utils.Utils.getRouteString
import org.emakeeva.testing.utils.Utils.getMatrixString
import org.emakeeva.testing.workers.AlgorithmWorker
import org.emakeeva.testing.workers.GraphWorker

fun main(args: Array<String>) {
    val worker = GraphWorker()
    val algorithmWorker = AlgorithmWorker()
    worker.createGraph(5, 1.0, 100.0)
    val graph = worker.getGraph()
    val path = graph?.let {
        algorithmWorker.getPath(it)
    }

    println("=====================")
    graph?.let {
        println("Количество вершин:")
        println(graph.numberOfVertices)
        println("Матрица расстояний:")
        println(getMatrixString(graph.adjacencyMatrix))
    } ?: println("Граф не был инициализирован")

    path?.let {
        println("Расстояние маршрута:")
        println(String.format("%.2f", path.distance))
        println("Оптимальный маршрут:")
        println(getRouteString(path.route))
    } ?: println("Что - то пошло не так при вычислении оптимального маршрута")
    println("=====================")
}