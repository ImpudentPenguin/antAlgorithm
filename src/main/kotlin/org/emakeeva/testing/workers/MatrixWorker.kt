package org.emakeeva.testing.workers

import org.emakeeva.testing.utils.MatrixInitException

class MatrixWorker {
    fun initMatrix(size: Int, value: Double): MutableList<MutableList<Double>> {
        validateMatrix(size, value)
        return MutableList(size) { MutableList(size) { value } }
    }

    private fun validateMatrix(size: Int, value: Double) {
        when {
            size !in 2..15 -> throw MatrixInitException("Matrix initialization error")
            value < 0.0 -> throw MatrixInitException("Matrix initialization with negative value is not possible")
        }
    }
}