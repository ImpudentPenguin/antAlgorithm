package org.emakeeva.testing

import org.emakeeva.testing.utils.Utils.getRouteString
import org.emakeeva.testing.utils.Utils.getMatrixString
import org.emakeeva.testing.workers.AlgorithmWorker
import org.emakeeva.testing.workers.GraphWorker
import java.io.BufferedReader
import java.io.InputStreamReader

fun main(args: Array<String>) {
    val worker = GraphWorker()
    val algorithmWorker = AlgorithmWorker()
    val reader = BufferedReader(InputStreamReader(System.`in`))
    println("Введите количество вершин (от 2 до 10 включительно):")
    var str = reader.readLine()

    while (!str.matches(Regex("^[2-9]|1[0]"))) {
        println("Пожалуйста, повторите ввод. Вероятно, вы ввели не число, либо вы ввели число вне диапазона.")
        str = reader.readLine()
    }

    reader.close()

    val vertices = Integer.parseInt(str)
    println("Матрица смежности заполняется автоматически.")
    worker.createGraph(vertices, 1.0, 100.0)
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