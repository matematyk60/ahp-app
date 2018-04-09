package com.dgodek.ahp.io

import com.dgodek.ahp.model.*

class AhpMapper {
    fun fromDto(dto: AhpModelDto): AhpModel {
        val choices = dto.CHOICE
        val mainCriterion = dto.CRITERION

        return AhpModel(choices, getCriterion(mainCriterion, choices.size))
    }

    fun fromModel(model: AhpModel): AhpModelDto {
        return AhpModelDto(model.choices,
                resolveCriterionDto(model.rootCriterion))

    }

    private fun getCriterion(criterionDto: CriterionDto, choices: Int): Criterion {
        return if (criterionDto.CRITERION.isEmpty()) {
            val crit = FinalCriterion(criterionDto.name, simpleMatrixFromString(criterionDto.m))
            validateFinalCrit(crit, choices)

            crit
        } else {
            val crit = NonFinalCriterion(criterionDto.name,
                    simpleMatrixFromString(criterionDto.m),
                    criterionDto.CRITERION.map {
                        getCriterion(it, choices)
                    })
            validateNonFinalCrit(crit)

            crit
        }
    }

    private fun validateFinalCrit(crit: FinalCriterion, choices: Int) {
        if (crit.matrix.numRows() != choices || crit.matrix.numCols() != choices) {
            throw IllegalArgumentException("Final criterion matrix dimensions must be equal to choices number")
        }
    }

    private fun validateNonFinalCrit(crit: NonFinalCriterion) {
        if (crit.matrix.numRows() != crit.subCriterion.size || crit.matrix.numCols() != crit.subCriterion.size) {
            throw IllegalArgumentException("Criterions matrix dimensions must match subcriterions number")
        }
    }

    private fun resolveCriterionDto(criterion: Criterion): CriterionDto = when (criterion) {
        is FinalCriterion -> CriterionDto(criterion.name,
                criterion.matrix.toCustomString(),
                emptyList())
        is NonFinalCriterion -> CriterionDto(criterion.name,
                criterion.matrix.toCustomString(),
                criterion.subCriterion.map { resolveCriterionDto(it) })
    }
}


