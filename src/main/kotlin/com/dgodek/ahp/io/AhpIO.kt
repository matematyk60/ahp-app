package com.dgodek.ahp.io

import java.io.FileReader
import java.io.FileWriter
import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller

class AhpIO {

    private val context = JAXBContext.newInstance(AhpModelDto::class.java)

    fun readFromFile(filename: String): AhpModelDto {
        val unmarsh = context.createUnmarshaller()
        val reader = FileReader("saved/$filename")

        return unmarsh.unmarshal(reader) as AhpModelDto
    }

    fun writeToFile(model: AhpModelDto, filename: String) {
        val marsh = context.createMarshaller()
        marsh.setProperty(Marshaller.JAXB_FRAGMENT, java.lang.Boolean.TRUE)
        val writer = FileWriter("saved/$filename")

        marsh.marshal(model, writer)
    }
}