package org.emakeeva.testing

import com.nhaarman.mockitokotlin2.description
import com.sun.org.glassfish.gmbal.Description
import org.emakeeva.testing.api.Graph
import org.emakeeva.testing.utils.*
import org.emakeeva.testing.workers.AlgorithmWorker
import org.emakeeva.testing.workers.MatrixWorker
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.eq
import org.mockito.junit.MockitoJUnitRunner

/**
 * Тестирование методов класса AlgorithmWorker
 */

@RunWith(MockitoJUnitRunner::class)
class AlgorithmWorkerTest {

    @Mock
    lateinit var mockMatrixWorker: MatrixWorker

    @Test
    @Description("TASD-1")
    fun `call getPath should return success result`() {
        algorithmWorker.matrixWorker = mockMatrixWorker
        `when`(mockMatrixWorker.initMatrix(eq(6), eq(0.1))).thenReturn(MutableList(6) { MutableList(6) { 0.1 } })

        val graph = Graph(6, listOf(
                listOf(0.0, 1.0, 5.0, 52.0, 25.0, 8.0),
                listOf(1.0, 0.0, 2.0, 5.0, 5.0, 2.0),
                listOf(5.0, 2.0, 0.0, 14.0, 12.0, 33.0),
                listOf(52.0, 5.0, 14.0, 0.0, 5.0, 5.0),
                listOf(25.0, 5.0, 12.0, 5.0, 0.0, 99.0),
                listOf(8.0, 2.0, 33.0, 5.0, 99.0, 0.0)
        ))

        val result = algorithmWorker.getPath(graph)
        assertEquals(result.distance, 30.0, 0.0)
    }

    @Test
    @Description("TASD-2")
    fun `call getPath with emptyList edges should return GraphHasntEdgesException`() {
        val graph = Graph(5, emptyList())
        assertThrows(GraphHasntEdgesException::class.java) {
            algorithmWorker.getPath(graph)
        }
    }

    @Test
    @Description("TASD-3")
    fun `call getPath with negative distance should return GraphNegativeDistanceException`() {
        val graph = Graph(
                5, listOf(
                listOf(0.0, 6.0, 100.0, 81.0, 23.0),
                listOf(6.0, 0.0, -1.0, 71.0, 0.0),
                listOf(100.0, 0.0, 0.0, 0.0, 0.0),
                listOf(81.0, 71.0, 0.0, 0.0, 0.0),
                listOf(23.0, 0.0, 0.0, 0.0, 0.0)))
        assertThrows(GraphNegativeDistanceException::class.java) {
            algorithmWorker.getPath(graph)
        }
    }

    @Test
    @Description("TASD-4")
    fun `call getPath with an incorrect dimension should return GraphIncorrectDimensionException`() {
        val graph = Graph(5, listOf(
                listOf(0.0, 0.0, 0.0, 79.0, 1.0),
                listOf(0.0, 0.0, 0.0, 71.0, 1.0),
                listOf(0.0, 0.0, 0.0, 79.0, 1.0)
        ))
        assertThrows(GraphIncorrectDimensionException::class.java) {
            algorithmWorker.getPath(graph)
        }
    }

    @Test
    @Description("TASD-5")
    fun `call getPath with an asymmetric matrix should return MatrixIsNotSymmetricException`() {
        val graph = Graph(4, listOf(
                listOf(0.0, 21.0, 31.0, 79.0),
                listOf(11.0, 0.0, 32.0, 71.0),
                listOf(12.0, 23.0, 0.0, 79.0),
                listOf(79.0, 71.0, 79.0, 0.0)
        ))
        assertThrows(MatrixIsNotSymmetricException::class.java) {
            algorithmWorker.getPath(graph)
        }
    }

    @Test
    @Description("TASD-6")
    fun `call getPath with an zeros distance should return MatrixZerosDistanceException`() {
        val graph = Graph(5, List(5) { List(5) { 0.0 } })
        assertThrows(MatrixZerosDistanceException::class.java) {
            algorithmWorker.getPath(graph)
        }
    }

    @Test
    @Description("TASD-7")
    fun `call getPath with one vertex should return GraphHasOneVertexException`() {
        val graph = Graph(1, List(3) { List(3) { 1.0 } })
        assertThrows(GraphHasOneVertexException::class.java) {
            algorithmWorker.getPath(graph)
        }
    }

    @Test
    @Description("TASD-8")
    fun `call getPath without vertex should return GraphHasntVerticesException`() {
        val graph = Graph(0, List(3) { List(3) { 1.0 } })
        assertThrows(GraphHasntVerticesException::class.java) {
            algorithmWorker.getPath(graph)
        }
    }

    companion object {
        private val algorithmWorker = AlgorithmWorker()
    }
}