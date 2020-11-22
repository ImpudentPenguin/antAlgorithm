package org.emakeeva.testing

import com.sun.org.glassfish.gmbal.Description
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.emakeeva.testing.api.*
import org.emakeeva.testing.utils.*
import org.emakeeva.testing.workers.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Response

/**
 * Интеграционное тестирование методов класса GraphWorker
 * с использованием заглушек на сервис GraphApiService
 */

@RunWith(MockitoJUnitRunner::class)
class GraphWorkerTest {

    @Mock
    private lateinit var mockGraphApiService: GraphApiService

    @Before
    fun setUp() {
        graphWorker.service = mockGraphApiService
    }

    @Test
    @Description("TASD-12")
    fun `call createGraph should return true`() {
        val graph = GraphBody(5,  10.0, 100.0)
        val mockCall = mock(Call::class.java) as Call<String>

        `when`(mockGraphApiService.createGraph(graph)).thenReturn(mockCall)
        `when`(mockCall.execute()).thenReturn(Response.success("created"))

        val result = graphWorker.createGraph(5,  10.0, 100.0)

        assertTrue(result)
        verify(mockGraphApiService, times(1)).createGraph(graph)
    }

    @Test
    @Description("TASD-23")
    fun `call createGraph without vertices should return GraphHasntVerticesException`() {
        val graph = GraphBody(0, 10.0, 100.0)
        val mockCall = mock(Call::class.java) as Call<String>

        `when`(mockGraphApiService.createGraph(graph)).thenReturn(mockCall)
        `when`(mockCall.execute()).thenReturn(Response.error(400, ResponseBody.create(MediaType.parse("application/json"), "{\"message\": \"Graph hasn't vertices\"}")))

        assertThrows(GraphHasntVerticesException::class.java) {
            graphWorker.createGraph(0, 10.0, 100.0)
        }
    }

    @Test
    @Description("TASD-24")
    fun `call createGraph with one vertices should return GraphHasOneVertexException`() {
        val graph = GraphBody(1, 10.0, 100.0)
        val mockCall = mock(Call::class.java) as Call<String>

        `when`(mockGraphApiService.createGraph(graph)).thenReturn(mockCall)
        `when`(mockCall.execute()).thenReturn(Response.error(400, ResponseBody.create(MediaType.parse("application/json"), "{\"message\": \"Graph has one vertex\"}")))

        assertThrows(GraphHasOneVertexException::class.java) {
            graphWorker.createGraph(1, 10.0, 100.0)
        }
    }

    @Test
    @Description("TASD-25")
    fun `call createGraph with many vertices should return GraphLimitVerticesException`() {
        val graph = GraphBody(20, 10.0, 100.0)
        val mockCall = mock(Call::class.java) as Call<String>

        `when`(mockGraphApiService.createGraph(graph)).thenReturn(mockCall)
        `when`(mockCall.execute()).thenReturn(Response.error(400, ResponseBody.create(MediaType.parse("application/json"), "{\"message\": \"Limit vertices of the graph\"}")))

        assertThrows(GraphLimitVerticesException::class.java) {
            graphWorker.createGraph(20, 10.0, 100.0)
        }
    }

    @Test
    @Description("TASD-26")
    fun `call createGraph with negative distance should return GraphNegativeDistanceException`() {
        val graph = GraphBody(5, -1.0, 100.0)
        val mockCall = mock(Call::class.java) as Call<String>

        `when`(mockGraphApiService.createGraph(graph)).thenReturn(mockCall)
        `when`(mockCall.execute()).thenReturn(Response.error(400, ResponseBody.create(MediaType.parse("application/json"), "{\"message\": \"Distance: going beyond boundaries\"}")))

        assertThrows(GraphNegativeDistanceException::class.java) {
            graphWorker.createGraph(5, -1.0, 100.0)
        }
    }

    @Test
    @Description("TASD-27")
    fun `call createGraph with incorrect distance should return GraphIncorrectDistanceException`() {
        val graph = GraphBody(5, 1.0, 300.0)
        val mockCall = mock(Call::class.java) as Call<String>

        `when`(mockGraphApiService.createGraph(graph)).thenReturn(mockCall)
        `when`(mockCall.execute()).thenReturn(Response.error(400, ResponseBody.create(MediaType.parse("application/json"), "{\"message\": \"Distance: going beyond boundaries\"}")))

        assertThrows(GraphIncorrectDistanceException::class.java) {
            graphWorker.createGraph(5, 1.0, 300.0)
        }
    }

    @Test
    @Description("TASD-13")
    fun `call getGraph should return graph`() {
        val graph = Graph(5, listOf(
                listOf(0.0, 6.0, 100.0, 81.0, 23.0),
                listOf(6.0, 0.0, 0.0, 71.0, 0.0),
                listOf(100.0, 0.0, 0.0, 0.0, 0.0),
                listOf(81.0, 71.0, 0.0, 0.0, 0.0),
                listOf(23.0, 0.0, 0.0, 0.0, 0.0)))
        val mockCall = mock(Call::class.java) as Call<Graph>

        `when`(mockGraphApiService.getGraph()).thenReturn(mockCall)
        `when`(mockCall.execute()).thenReturn(Response.success(graph))

        val result = graphWorker.getGraph()

        assertEquals(result, graph)
        verify(mockGraphApiService, times(1)).getGraph()
    }

    @Test
    @Description("TASD-14")
    fun `call getGraph should return null`() {
        val mockCall = mock(Call::class.java) as Call<Graph>

        `when`(mockGraphApiService.getGraph()).thenReturn(mockCall)
        `when`(mockCall.execute()).thenReturn(Response.error(404, ResponseBody.create(MediaType.parse("application/json"), "{\"message\": \"Graph not found\"}")))

        val result = graphWorker.getGraph()

        assertNull(result)
        verify(mockGraphApiService, times(1)).getGraph()
    }

    companion object {
        private val graphWorker = GraphWorker()
    }
}