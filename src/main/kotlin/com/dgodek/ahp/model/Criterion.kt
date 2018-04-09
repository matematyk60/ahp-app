package com.dgodek.ahp.model

import org.ejml.simple.SimpleMatrix
import org.slf4j.LoggerFactory


sealed class Criterion(val name: String, val matrix: SimpleMatrix) {
    fun name(): String = name

    abstract fun priorityVector(func: SimpleMatrix.() -> SimpleMatrix): SimpleMatrix
}

class FinalCriterion(name: String, matrix: SimpleMatrix) : Criterion(name, matrix) {
    override fun priorityVector(func: SimpleMatrix.() -> SimpleMatrix): SimpleMatrix {
        val vector = matrix.func()

        logger.debug("Returning priority vector ${vector.toCustomString()}, sum: ${vector.elementSum()}")
        return vector
    }

    companion object {
        val logger = LoggerFactory.getLogger(FinalCriterion::class.java)
    }
}

class NonFinalCriterion(name: String, matrix: SimpleMatrix,
                        val subCriterion: List<Criterion>) : Criterion(name, matrix) {

    override fun priorityVector(func: SimpleMatrix.() -> SimpleMatrix): SimpleMatrix {
        val outerVector = matrix.func()

        val c = subCriterion.mapIndexed { i, crit ->
            crit.priorityVector(func)
                    .mult(SimpleMatrix(1, 1).apply {
                        set(0, 0, outerVector.get(i, 0))
                    })
        }.joinSumming()

        logger.debug("Returning priority Vector ${c.toCustomString()}, sum: ${c.elementSum()}")

        return c
    }

    companion object {
        val logger = LoggerFactory.getLogger(NonFinalCriterion::class.java)
    }
}

fun List<SimpleMatrix>.joinSumming(): SimpleMatrix {
    var sum = get(0).plus(get(1))

    (1 until lastIndex)
            .forEach { sum = sum.plus(get(it + 1)) }

    return sum
}
