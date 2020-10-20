package org.emakeeva.testing.workers

import org.emakeeva.testing.api.Graph
import org.emakeeva.testing.models.Ant
import org.emakeeva.testing.models.Path
import org.emakeeva.testing.utils.*
import java.lang.RuntimeException
import kotlin.math.pow
import kotlin.random.Random

class AlgorithmWorker {

    var matrixWorker = MatrixWorker()
    private var distances: List<List<Double>> = mutableListOf()
    private var bestPath = Path(Double.MAX_VALUE) // лучший путь
    private var alpha = 0.5 // коэффициент жадности, влияние феромона
    private var beta = 0.5 // коэффициент стадности, влияние длины пути
    private var q = 0.0 // параметр, имеющий значение порядка длины оптимального пути
    private var tau = 1.0 //  количество испарения феромона
    private var countIteration = 1000 // количество итераций
    private var trails: MutableList<MutableList<Double>> = mutableListOf() // матрица феромона
    private var probability: MutableList<Double> = mutableListOf()
    private var ants: MutableList<Ant> = mutableListOf()
    private var currentIndex: Int = -1
    private var size: Int = -1

    fun changeParams(alfa: Double, beta: Double, q: Double, tau: Double, countIteration: Int) {
        this.alpha = alfa
        this.beta = beta
        this.q = q
        this.tau = tau
        this.countIteration = countIteration
    }

    fun getPath(graph: Graph): Path {
        validateGraph(graph)
        initParams(graph)

        for (t in 0 until countIteration) {
            setupAnts()
            moveAnts()
            updateTrails()
            updateBestPath()
        }

        return bestPath
    }

    private fun initParams(graph: Graph) {
        size = graph.numberOfVertices
        distances = graph.adjacencyMatrix
        q = (((size * size) - size) / size).toDouble()
        ants = initAnts(graph, size)
        trails = matrixWorker.initMatrix(size, 0.1)
        probability = MutableList(size) { 0.0 }
    }

    private fun setupAnts() {
        currentIndex = -1
        for (i in 0 until size) {
            ants[i].clear(size)
            ants[i].visitTown(Random.nextInt(size), currentIndex)
        }
        currentIndex++
    }

    private fun moveAnts() {
        while (currentIndex < size - 1) {
            for (ant in ants) {
                ant.visitTown(selectNextTown(ant, size), currentIndex)
            }
            currentIndex++
        }
    }

    private fun updateBestPath() {
        if (bestPath.route.isNullOrEmpty()) {
            bestPath.route = ants[0].tour
            bestPath.distance = ants[0].tourLength(size)
        }

        for (ant in ants) {
            if (ant.tourLength(size) < bestPath.distance) {
                bestPath.distance = ant.tourLength(size)
                bestPath.route = ArrayList(ant.tour)
            }
        }
    }

    private fun updateTrails() {
        for (i in 0 until size)
            for (j in 0 until size)
                trails[i][j] *= tau

        for (ant in ants) {
            val contribution = q / ant.tourLength(size)
            for (i in 0 until size - 1)
                trails[ant.tour[i]][ant.tour[i + 1]] += contribution
            trails[ant.tour[size - 1]][ant.tour[0]] += contribution
        }
    }

    private fun selectNextTown(ant: Ant, size: Int): Int {
        val curCity = ant.tour[currentIndex]
        var sum = 0.0

        for (city in 0 until size)
            if (!ant.visited[city] && distances[curCity][city] != 0.0)
                sum += trails[curCity][city].pow(alpha) * (1.0 / distances[curCity][city]).pow(beta)

        for (j in 0 until size) {
            if (ant.visited[j])
                probability[j] = 0.0
            else {
                val numerator = trails[curCity][j].pow(alpha) * (1.0 / distances[curCity][j]).pow(beta)
                probability[j] = numerator / sum
            }
        }

        val next = Random.nextDouble()
        var tot = 0.0
        for (i in 0 until size) {
            tot += probability[i]
            if (tot >= next)
                return i
        }

        throw RuntimeException()
    }

    private fun validateGraph(graph: Graph) {
        when {
            graph.adjacencyMatrix.size != graph.adjacencyMatrix.firstOrNull()?.size ?: 0 -> throw GraphIncorrectDimensionException()
            graph.numberOfVertices <= 0 -> throw GraphHasntVerticesException()
            graph.numberOfVertices == 1 -> throw GraphHasOneVertexException()
            graph.adjacencyMatrix.isEmpty() -> throw GraphHasntEdgesException()
            graph.adjacencyMatrix.toString().contains(Regex("-\\d")) -> throw GraphNegativeDistanceException()
            checkMatrixSymmetry(graph) -> throw MatrixIsNotSymmetricException()
            checkMatrixForZeroDistance(graph) -> throw MatrixZerosDistanceException()
        }
    }

    private fun checkMatrixSymmetry(graph: Graph): Boolean {
        var flag = false
        for (i in 0 until graph.numberOfVertices) {
            for (j in 0 until graph.numberOfVertices) {
                if (graph.adjacencyMatrix[i][j] != graph.adjacencyMatrix[j][i])
                    flag = true
            }
        }

        return flag
    }

    private fun checkMatrixForZeroDistance(graph: Graph): Boolean {
        var isZeroDistance = true
        graph.adjacencyMatrix.forEach {
            it.forEach { distance ->
                if (distance != 0.0)
                    isZeroDistance = false
            }
        }

        return isZeroDistance
    }

    private fun initAnts(graph: Graph, size: Int): MutableList<Ant> = MutableList(size) { Ant(graph) }
}