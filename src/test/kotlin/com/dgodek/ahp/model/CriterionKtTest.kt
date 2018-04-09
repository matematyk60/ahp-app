package com.dgodek.ahp.model

import org.junit.Test

import org.junit.Assert.*

class CriterionKtTest {

    @Test
    fun fromString() {
        val m = simpleMatrixFromString("1.0 3.0 4.0; 0.333333333333 1.0 5.0; 0.25 0.2 1.0")

        assertEquals(1.0, m[0, 0], 0.01)
        assertEquals(5.0, m[1, 2], 0.01)
        assertEquals(1.0, m[2, 2], 0.01)
    }
}