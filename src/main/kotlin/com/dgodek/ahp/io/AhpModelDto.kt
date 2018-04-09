package com.dgodek.ahp.io

import javax.xml.bind.annotation.*

@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlRootElement(name = "CRITERION")
@XmlType(propOrder = arrayOf("name", "m", "CRITERION"))
class CriterionDto(@XmlAttribute val name: String = "",
                   @XmlAttribute val m: String = "",
        //below should be named 'criterions', but JaxB and Jackson went crazy
                   val CRITERION: List<CriterionDto> = mutableListOf())

@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlRootElement(name = "ROOT")
class AhpModelDto(
        //below should be named 'choices', but JaxB and Jackson went crazy
        val CHOICE: List<String> = mutableListOf(),
        //below should be named 'mainCriterion', but JaxB and Jackson went crazy
        val CRITERION: CriterionDto = CriterionDto())