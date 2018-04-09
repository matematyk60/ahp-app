package com.dgodek.ahp.app

import com.dgodek.ahp.app.MainState.*
import com.dgodek.ahp.app.ModelState.*
import com.dgodek.ahp.generator.Generator
import com.dgodek.ahp.io.AhpResolver
import com.dgodek.ahp.model.AhpModel
import com.dgodek.ahp.model.priorityVectorEigen
import com.dgodek.ahp.model.priorityVectorGeom
import org.ejml.simple.SimpleMatrix

enum class MainState {
    READ_FROM_FILE,
    GENERATE_FROM_FINGER,
    EXIT
}

enum class ModelState {
    CALCULATE_PRIORITY_VECTOR,
    CALCULATE_PRIORITY_VECTOR_GEOM,
    WRITE_TO_FILE,
    EXIT
}

class AhpApp {

    private lateinit var model: AhpModel

    private val generator = Generator()
    private val ahpResolver = AhpResolver()

    tailrec fun mainView() {
        when (selectActionMain()) {
            READ_FROM_FILE -> modelView(modelFromFile())
            GENERATE_FROM_FINGER -> modelView(generator.generate())
            MainState.EXIT -> return
        }

        mainView()
    }

    private tailrec fun modelView(model: AhpModel) {
        this.model = model

        when (selectActionModel()) {
            CALCULATE_PRIORITY_VECTOR -> {
                model.calculate(SimpleMatrix::priorityVectorEigen)
            }
            CALCULATE_PRIORITY_VECTOR_GEOM -> {
                model.calculate(SimpleMatrix::priorityVectorGeom)
            }
            WRITE_TO_FILE -> modelToFile(model)
            ModelState.EXIT -> return
        }

        modelView(model)
    }

    private fun modelToFile(model: AhpModel) {
        println("Please type path save to model file")
        val filename = readLine()

        ahpResolver.writeModelToFile(model, filename!!)
    }

    private fun modelFromFile(): AhpModel {
        println("Please type path to model file")
        val filename = readLine()

        return ahpResolver.readModelFromFile(filename!!)
    }

    private tailrec fun selectActionMain(): MainState {
        println("Select action:\n" +
                "(1)Read model from file\n" +
                "(2)Start generation!\n" +
                "(3)exit")

        return when (readLine()) {
            "1" -> READ_FROM_FILE
            "2" -> GENERATE_FROM_FINGER
            "3" -> MainState.EXIT
            else -> selectActionMain()
        }
    }

    private tailrec fun selectActionModel(): ModelState {
        println("Select action:\n" +
                "(1)Calculate priority vector (EigenVector method)\n" +
                "(2)Calculate priority vector (Geometrical mean method)\n" +
                "(3)save to file!\n" +
                "(4)exit")

        return when (readLine()) {
            "1" -> CALCULATE_PRIORITY_VECTOR
            "2" -> CALCULATE_PRIORITY_VECTOR_GEOM
            "3" -> WRITE_TO_FILE
            "4" -> ModelState.EXIT
            else -> selectActionModel()
        }
    }

}