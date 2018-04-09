package com.dgodek.ahp.io

import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.StringReader
import java.io.StringWriter
import java.util.*
import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBException
import javax.xml.bind.Marshaller

class CriterionDtoTest @Throws(JAXBException::class)
constructor() {

    private val context = JAXBContext.newInstance(CriterionDto::class.java)
    @Test
    @Throws(JAXBException::class)
    fun shouldSerialize() {
        val c1 = CriterionDto("BABA", "cos")
        val c2 = CriterionDto("LALA", "cos2")
        val criterion = CriterionDto("w", "m",
                Arrays.asList(c1, c2))
        val m = context.createMarshaller()
        val writer = StringWriter()
        m.setProperty(Marshaller.JAXB_FRAGMENT, java.lang.Boolean.TRUE)
        m.marshal(criterion, writer)

        val serialized = writer.toString()

        println(serialized)

        assertEquals("<CRITERION name=\"w\" m=\"m\"><CRITERION name=\"BABA\" m=\"cos\"/><CRITERION name=\"LALA\" m=\"cos2\"/></CRITERION>", serialized)

    }

    @Test
    @Throws(JAXBException::class)
    fun shouldDeserialize() {
        val u = context.createUnmarshaller()
        val v = "<CRITERION name=\"w\" m=\"m\"><CRITERION name=\"BABA\" m=\"cos\"/><CRITERION name=\"LALA\" m=\"cos2\"/></CRITERION>"
        val reader = StringReader(v)
        val criterion = u.unmarshal(reader) as CriterionDto

        assertEquals("w", criterion.name)
        assertEquals("m", criterion.m)
        assertEquals("BABA", criterion.CRITERION[0].name)
        assertEquals("cos", criterion.CRITERION[0].m)
        assertEquals("LALA", criterion.CRITERION[1].name)
        assertEquals("cos2", criterion.CRITERION[1].m)
    }


    @Test
    @Throws(JAXBException::class)
    fun shouldDeserializeWithoutList() {
        val u = context.createUnmarshaller()
        val v = "<CRITERION name=\"Attractions\" m=\"1.0 4.0 6.0; 0.25 1.0 4.0; 0.166666666667 0.25 1.0\"/>"
        val reader = StringReader(v)
        val criterion = u.unmarshal(reader) as CriterionDto

        assertEquals("Attractions", criterion.name)
        assertEquals("1.0 4.0 6.0; 0.25 1.0 4.0; 0.166666666667 0.25 1.0", criterion.m)
    }

    @Test
    @Throws(JAXBException::class)
    fun shouldSerializeWithList() {
        val criterion = CriterionDto("Attractions", "1.0 4.0 6.0; 0.25 1.0 4.0; 0.166666666667 0.25 1.0")
        val m = context.createMarshaller()
        val writer = StringWriter()
        m.setProperty(Marshaller.JAXB_FRAGMENT, java.lang.Boolean.TRUE)
        m.marshal(criterion, writer)


        val serialized = writer.toString()

        println(serialized)

        assertEquals("<CRITERION name=\"Attractions\" m=\"1.0 4.0 6.0; 0.25 1.0 4.0; 0.166666666667 0.25 1.0\"/>", serialized)

    }


}