package com.dgodek.ahp.model

import org.junit.Assert.*
import org.junit.Test
import kotlin.math.pow

class UtilsKtTest {

    @Test
    fun shouldParse() {
        val s = "1.0 4.0 6.0; 0.25 1.0 4.0; 0.166666666667 0.25 1.0"
        val matrix = simpleMatrixFromString(s)

        assertEquals(1.0, matrix[0, 0], 0.1)
        assertEquals(6.0, matrix[0, 2], 0.1)
        assertEquals(0.25, matrix[1, 0], 0.1)
        assertEquals(4.0, matrix[1, 2], 0.1)
        assertEquals(0.25, matrix[2, 1], 0.1)
    }

    @Test
    fun souldUnparse() {
        val s = "1.0 4.0 6.0; 0.25 1.0 4.0; 0.166666666667 0.25 1.0"
        val matrix = simpleMatrixFromString(s)

        println(matrix.toCustomString())
    }

    @Test
    fun multTest() {
        val s = "1.0 4.0 6.0; 0.25 1.0 4.0; 0.166666666667 0.25 1.0"
        val matrix = simpleMatrixFromString(s)

        assertEquals(24.0, matrix.getMultForRow(0), 0.01)
        assertEquals(1.0, matrix.getMultForRow(1), 0.01)
    }

    @Test
    fun shouldFindNormalizationTest() {
        val s = "1.0 4.0 6.0; 0.25 1.0 4.0; 0.166666666667 0.25 1.0"
        val matrix = simpleMatrixFromString(s)

        var sum = matrix.getMultForRow(0).pow(1.0 / matrix.numRows()) +
                matrix.getMultForRow(1).pow(1.0 / matrix.numRows()) +
                matrix.getMultForRow(2).pow(1.0 / matrix.numRows())


        assertEquals(sum, matrix.normalizationTerm(), 0.01)
    }
}