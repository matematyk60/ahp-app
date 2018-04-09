package com.dgodek.ahp.io

import com.dgodek.ahp.model.AhpModel

class AhpResolver {

    private val mapper = AhpMapper()
    private val io = AhpIO()

    fun writeModelToFile(model: AhpModel, filename: String) {
        val dto = mapper.fromModel(model)

        io.writeToFile(dto, filename)
    }

    fun readModelFromFile(filename: String): AhpModel {
        val dto = io.readFromFile(filename)

        return mapper.fromDto(dto)
    }

}