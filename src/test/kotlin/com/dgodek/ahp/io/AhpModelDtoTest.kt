package com.dgodek.ahp.io

import org.junit.Assert.*
import org.junit.Test
import java.io.StringReader
import java.io.StringWriter
import java.util.*
import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBException
import javax.xml.bind.Marshaller

class AhpModelDtoTest {

    private val context = JAXBContext.newInstance(AhpModelDto::class.java)

    @Test
    @Throws(JAXBException::class)
    fun shouldSerialize() {
        val c1 = CriterionDto("BABA", "cos")
        val c2 = CriterionDto("LALA", "cos2")
        val criterion = CriterionDto("w", "m",
                Arrays.asList(c1, c2))
        val ahpDto = AhpModelDto(listOf("Barcelona", "Nowa Zelandia"), criterion)
        val m = context.createMarshaller()
        val writer = StringWriter()
        m.setProperty(Marshaller.JAXB_FRAGMENT, java.lang.Boolean.TRUE)
        m.marshal(ahpDto, writer)
        val serialized = writer.toString()

        println(serialized)
        assertEquals("<ROOT><CHOICE>Barcelona</CHOICE><CHOICE>Nowa Zelandia</CHOICE><CRITERION name=\"w\" m=\"m\"><CRITERION name=\"BABA\" m=\"cos\"/><CRITERION name=\"LALA\" m=\"cos2\"/></CRITERION></ROOT>",
                serialized)
    }

    @Test
    @Throws(JAXBException::class)
    fun shouldDeserialize() {
        val u = context.createUnmarshaller()
        val v = "<ROOT><CHOICE>Barcelona</CHOICE><CHOICE>Nowa Zelandia</CHOICE><CRITERION name=\"w\" m=\"m\"><CRITERION name=\"BABA\" m=\"cos\"/><CRITERION name=\"LALA\" m=\"cos2\"/></CRITERION></ROOT>"
        val reader = StringReader(v)
        val ahp = u.unmarshal(reader) as AhpModelDto

        assertEquals("Barcelona", ahp.CHOICE[0])
        assertEquals("Nowa Zelandia", ahp.CHOICE[1])
        assertEquals("w", ahp.CRITERION.name)
        assertEquals("m", ahp.CRITERION.m)
        assertEquals("BABA", ahp.CRITERION.CRITERION[0].name)
        assertEquals("cos", ahp.CRITERION.CRITERION[0].m)
        assertEquals("LALA", ahp.CRITERION.CRITERION[1].name)
        assertEquals("cos2", ahp.CRITERION.CRITERION[1].m)
    }
}