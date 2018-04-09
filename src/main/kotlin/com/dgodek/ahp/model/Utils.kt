package com.dgodek.ahp.model

import org.ejml.simple.SimpleMatrix
import java.text.DecimalFormat
import kotlin.math.pow

fun simpleMatrixFromString(string: String): SimpleMatrix {
    val arrayOfRows = string.split(";")
            .map {
                it.trim().split(" ")
                        .map { it.toDoublePossibleStraight() }
                        .toDoubleArray()
            }
            .toTypedArray()
    return SimpleMatrix(arrayOfRows)
}

fun SimpleMatrix.priorityVectorEigen(): SimpleMatrix {
    val eig = this.eig()
    val vector = eig.getEigenVector(eig.indexMax)

    val sum = vector.elementSum()

    val priorityVector = SimpleMatrix(vector.numRows(), 1)
    vector.matrix.data.forEachIndexed { i, v ->
        priorityVector.set(i, 0, v / sum)
    }

    return priorityVector
}

fun SimpleMatrix.priorityVectorGeom(): SimpleMatrix {
    val norm = normalizationTerm()
    val priorityVector = SimpleMatrix(numRows(), 1)
    (0 until numRows()).forEach {
        val mult = getMultForRow(it).pow((1.0 / numRows()))
        priorityVector.set(it, 0, mult / norm)
    }

    return priorityVector
}

fun SimpleMatrix.normalizationTerm(): Double {
    var sum = 0.0
    (0 until numRows()).forEach {
        sum += getMultForRow(it).pow(1.0 / numRows())
    }

    return sum
}

fun SimpleMatrix.getMultForRow(rowNuber: Int): Double {
    var mult = 1.0
    (0 until numCols()).forEach { i ->
        mult *= get(rowNuber, i)
    }

    return mult
}

fun SimpleMatrix.toCustomString(): String {
    val numCols = this.matrix.numCols
    val format = DecimalFormat("#.############")
    return this.matrix.data.mapIndexed { i, d ->
        if (i % numCols == numCols - 1) "${format.format(d)};" else format.format(d)
    }
            .joinToString(" ").dropLast(1)
}

fun String.toDoublePossibleStraight() = if (this.contains('/')) {
    val splitted = this.split('/')
            .map { it.toDouble() }
    splitted[0] / splitted[1]
} else {
    this.toDouble()
}

