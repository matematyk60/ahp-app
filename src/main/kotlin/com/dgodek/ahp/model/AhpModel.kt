package com.dgodek.ahp.model

import org.ejml.simple.SimpleMatrix

class AhpModel(val choices: List<String>, val rootCriterion: Criterion) {
    fun calculate(func: SimpleMatrix.() -> SimpleMatrix) {
        val vector = rootCriterion.priorityVector(func)
        choices.forEachIndexed { i, v -> println(v + ": " + vector.get(i, 0)) }
    }
}