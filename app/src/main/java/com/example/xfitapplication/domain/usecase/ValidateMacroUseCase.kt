package com.example.xfitapplication.domain.usecase

import com.example.xfitapplication.domain.validation.FieldValidationResult
import com.example.xfitapplication.domain.validation.InputLimits
import com.example.xfitapplication.domain.validation.ValidationMessages

class ValidateMacroUseCase {

    enum class Macro(val label: String, val maxGrams: Int) {
        PROTEIN("Белки", InputLimits.MAX_PROTEIN_GRAMS),
        FAT("Жиры", InputLimits.MAX_FAT_GRAMS),
        CARBS("Углеводы", InputLimits.MAX_CARBS_GRAMS)
    }

    fun execute(macro: Macro, grams: Int): FieldValidationResult = when {
        grams < 0 -> FieldValidationResult.invalid("${macro.label} не могут быть отрицательными")
        grams > macro.maxGrams ->
            FieldValidationResult.invalid(ValidationMessages.maxExceeded(macro.maxGrams))
        else -> FieldValidationResult.valid()
    }
}
