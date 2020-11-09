package org.emakeeva.testing.utils

import java.io.BufferedReader
import java.io.InputStreamReader

object Utils {
    fun getRouteString(route: MutableList<Int>): String {
        val stringBuilder = StringBuilder("[")
        for (i in 0 until route.size) {
            if (i == route.size - 1)
                stringBuilder.append("(${route.last()}, ${route.first()})")
            else {
                stringBuilder.append("(${route[i]}, ${route[i + 1]})")
                stringBuilder.append(", ")
            }
        }
        stringBuilder.append("]")

        return stringBuilder.toString()
    }

    fun getMatrixString(matrix: List<List<Double>>): String {
        val stringBuilder = StringBuilder()
        for (element in matrix) {
            stringBuilder.append(element)
            stringBuilder.append("\n")
        }

        return stringBuilder.toString()
    }
}