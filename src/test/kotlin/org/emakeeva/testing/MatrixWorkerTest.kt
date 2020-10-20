package org.emakeeva.testing

import com.sun.org.glassfish.gmbal.Description
import org.emakeeva.testing.utils.MatrixInitException
import org.emakeeva.testing.workers.MatrixWorker
import org.junit.Test
import org.junit.Assert.*

class MatrixWorkerTest {

    @Test
    @Description("TASD-9")
    fun `call initMatrix should return matrix`() {
        val matrix = mutableListOf(
                mutableListOf(1.0, 1.0, 1.0, 1.0, 1.0),
                mutableListOf(1.0, 1.0, 1.0, 1.0, 1.0),
                mutableListOf(1.0, 1.0, 1.0, 1.0, 1.0),
                mutableListOf(1.0, 1.0, 1.0, 1.0, 1.0),
                mutableListOf(1.0, 1.0, 1.0, 1.0, 1.0)
        )
        val result = matrixWorker.initMatrix(5, 1.0)
        assertEquals(matrix, result)
    }

    @Test
    @Description("TASD-10")
    fun `call initMatrix with negative value should return MatrixInitException`() {
        assertThrows(MatrixInitException::class.java) {
            matrixWorker.initMatrix(5, -1.0)
        }
    }

    @Test
    @Description("TASD-11")
    fun `call initMatrix with size = 0 should return MatrixInitException`() {
        assertThrows(MatrixInitException::class.java) {
            matrixWorker.initMatrix(0, 1.0)
        }
    }

    companion object {
        private val matrixWorker = MatrixWorker()
    }
}