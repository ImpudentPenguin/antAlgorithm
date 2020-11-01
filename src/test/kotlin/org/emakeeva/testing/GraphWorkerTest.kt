package org.emakeeva.testing

import com.sun.org.glassfish.gmbal.Description
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.emakeeva.testing.api.Graph
import org.emakeeva.testing.api.GraphApiService
import org.emakeeva.testing.api.GraphBody
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
        `when`(mockCall.execute()).thenReturn(Response.error(400, ResponseBody.create(MediaType.parse("Graph not found"), "Graph not found")))

        val result = graphWorker.getGraph()

        assertNull(result)
        verify(mockGraphApiService, times(1)).getGraph()
    }

    companion object {
        private val graphWorker = GraphWorker()
    }
}