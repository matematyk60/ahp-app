package com.dgodek.ahp.generator

import com.dgodek.ahp.model.AhpModel
import com.dgodek.ahp.model.Criterion
import com.dgodek.ahp.model.FinalCriterion
import com.dgodek.ahp.model.NonFinalCriterion
import org.ejml.simple.SimpleMatrix

class Generator {

    fun generate(): AhpModel {
        val choices = learnChoices()
        val rootCriterion = learnCriterion(choices, "ROOT")

        return AhpModel(choices, rootCriterion)
    }

    private fun learnChoices(): List<String> {
        val list = mutableListOf<String>()
        var stopped = false
        while (!stopped) {
            list.add(inputRequest("an alternative"))
            stopped = !isAnyMore("alternatives")
        }

        return list
    }

    private fun learnCriterion(choices: List<String>, path: String): Criterion {
        println(path)
        return if (isMidCriterion()) {
            learnMidCriterion(choices, path)
        } else {
            learnFinalCriterion(choices, path)
        }
    }

    private fun learnMidCriterion(choices: List<String>, path: String): Criterion {
        println(path)
        val name = criterionNameRequest()

        val list = mutableListOf<Criterion>()

        var stopped = false
        while (!stopped) {
            list.add(learnCriterion(choices, "$path -> $name"))
            stopped = !isAnyMore("subcriterions")
        }

        println(path)
        val map = list.map { it to doubleRequest("a weight value for criterion ${it.name}") }.toMap()
        val matrix = SimpleMatrix(list.size, list.size)
        list.forEachIndexed { iOuter, vOuter ->
            list.forEachIndexed { iInner, vInner ->
                matrix.set(iOuter, iInner, map[vOuter]!! / map[vInner]!!)
            }
        }

        return NonFinalCriterion(name, matrix, list)
    }

    private fun learnFinalCriterion(choices: List<String>, path: String): Criterion {
        println(path)
        val name = criterionNameRequest()
        println("$path -> $name")
        val map = choices.map { it to doubleRequest("a weight value for choice $it") }.toMap()

        val matrix = SimpleMatrix(choices.size, choices.size)
        choices.forEachIndexed { iOuter, vOuter ->
            choices.forEachIndexed { iInner, vInner ->
                matrix.set(iOuter, iInner, map[vOuter]!! / map[vInner]!!)
            }
        }

        return FinalCriterion(name, matrix)
    }

    companion object {

        private fun logicCheck(message: String): Boolean {
            println("$message(y/n)")
            return when (readLine()) {
                "y" -> true
                "n" -> false
                else -> logicCheck(message)
            }
        }

        private fun inputRequest(message: String): String {
            println("Please type $message")
            return readLine()!!
        }

        private fun criterionNameRequest(): String = inputRequest("a name for Criterion")

        private fun doubleRequest(message: String): Double = inputRequest(message).toDouble()

        private fun isAnyMore(name: String): Boolean = logicCheck("Any more $name?")

        private fun isMidCriterion(): Boolean = logicCheck("Is it mid criterion?")

    }
}