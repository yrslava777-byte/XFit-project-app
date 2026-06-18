package com.example.xfitapplication.presentation.util

import android.content.Context
import android.widget.Toast
import com.example.xfitapplication.domain.validation.ValidationMessages
import com.google.android.material.textfield.TextInputEditText

data class DecimalFieldSpec(
    val field: TextInputEditText,
    val label: String,
    val validate: ((Double) -> String?)? = null
)

data class DecimalFieldsValidation(
    val values: List<Double?>,
    val hasErrors: Boolean,
    val errorMessages: List<String> = emptyList()
)

private data class ParsedDecimalField(val value: Double?, val error: String?)

fun validateDecimalFields(context: Context, specs: List<DecimalFieldSpec>): DecimalFieldsValidation {
    val result = applyDecimalFieldErrors(specs)
    if (result.hasErrors) {
        showFormErrors(context, result.errorMessages)
    }
    return result
}

fun applyDecimalFieldErrors(specs: List<DecimalFieldSpec>): DecimalFieldsValidation {
    val errors = mutableListOf<String>()
    val values = mutableListOf<Double?>()

    specs.forEach { spec ->
        val parsed = parseDecimalFromField(spec.field, spec.validate)
        values.add(parsed.value)
        if (parsed.error != null) {
            spec.field.setFieldError(parsed.error)
            errors.add("Поле «${spec.label}»: ${parsed.error}")
        } else {
            spec.field.setFieldError(null)
        }
    }

    return DecimalFieldsValidation(values, errors.isNotEmpty(), errors)
}

fun readProductNameField(
    context: Context,
    field: TextInputEditText,
    validate: (String) -> String?
): String? {
    val raw = field.text?.toString().orEmpty()
    val error = validate(raw)
    if (error != null) {
        field.setFieldError(error)
        showFormError(context, "Поле «Название»: $error")
        return null
    }
    field.setFieldError(null)
    return raw.trim()
}

private fun parseDecimalFromField(
    field: TextInputEditText,
    validate: ((Double) -> String?)? = null
): ParsedDecimalField {
    val raw = field.text?.toString()?.trim().orEmpty()
    if (raw.isEmpty()) {
        return ParsedDecimalField(null, ValidationMessages.INTEGER_ONLY)
    }
    if (raw.hasInvalidDecimalCharacters()) {
        return ParsedDecimalField(null, ValidationMessages.INVALID_CHARACTER)
    }
    val value = raw.parseStrictDecimal()
    if (value == null) {
        return ParsedDecimalField(null, ValidationMessages.INVALID_CHARACTER)
    }
    validate?.invoke(value)?.let { message ->
        return ParsedDecimalField(null, message)
    }
    return ParsedDecimalField(value, null)
}

data class IntFieldSpec(
    val field: TextInputEditText,
    val label: String,
    val validate: ((Int) -> String?)? = null
)

data class IntFieldsValidation(
    val values: List<Int?>,
    val hasErrors: Boolean,
    val errorMessages: List<String> = emptyList()
)

private data class ParsedIntField(val value: Int?, val error: String?)

fun validateIntFields(context: Context, specs: List<IntFieldSpec>): IntFieldsValidation {
    val result = applyIntFieldErrors(specs)
    if (result.hasErrors) {
        showFormErrors(context, result.errorMessages)
    }
    return result
}

fun applyIntFieldErrors(specs: List<IntFieldSpec>): IntFieldsValidation {
    val errors = mutableListOf<String>()
    val values = mutableListOf<Int?>()

    specs.forEach { spec ->
        val parsed = parseStrictIntFromField(spec.field, spec.validate)
        values.add(parsed.value)
        if (parsed.error != null) {
            spec.field.setFieldError(parsed.error)
            errors.add("Поле «${spec.label}»: ${parsed.error}")
        } else {
            spec.field.setFieldError(null)
        }
    }

    return IntFieldsValidation(values, errors.isNotEmpty(), errors)
}

fun showFormErrors(context: Context, errors: List<String>) {
    if (errors.isEmpty()) return
    Toast.makeText(context, errors.joinToString("\n"), Toast.LENGTH_LONG).show()
}

fun showFormError(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

private fun parseStrictIntFromField(
    field: TextInputEditText,
    validate: ((Int) -> String?)? = null
): ParsedIntField {
    val raw = field.text?.toString().orEmpty()
    if (raw.trim().isEmpty()) {
        return ParsedIntField(null, ValidationMessages.INTEGER_ONLY)
    }
    if (raw.hasInvalidIntegerCharacters()) {
        return ParsedIntField(null, ValidationMessages.INVALID_CHARACTER)
    }
    val value = raw.parseStrictInt()
    if (value == null) {
        return ParsedIntField(null, ValidationMessages.INVALID_CHARACTER)
    }
    validate?.invoke(value)?.let { message ->
        return ParsedIntField(null, message)
    }
    return ParsedIntField(value, null)
}

fun readPortionWeightField(
    context: Context,
    field: TextInputEditText,
    validate: (Double) -> String?
): Double? {
    val raw = field.text?.toString()?.trim().orEmpty()
    if (raw.isEmpty()) {
        field.setFieldError("Укажите вес порции")
        showFormError(context, "Укажите вес порции")
        return null
    }
    if (raw.hasInvalidDecimalCharacters()) {
        field.setFieldError(ValidationMessages.INVALID_CHARACTER)
        showFormError(context, ValidationMessages.INVALID_CHARACTER)
        return null
    }
    val weight = raw.parseStrictDecimal()
    if (weight == null || weight <= 0) {
        field.setFieldError("Укажите вес порции")
        showFormError(context, "Укажите вес порции")
        return null
    }
    validate(weight)?.let { message ->
        field.setFieldError(message)
        showFormError(context, message)
        return null
    }
    field.setFieldError(null)
    return weight
}
