package com.example.xfitapplication.presentation.util

import android.text.InputType
import android.text.method.DigitsKeyListener
import android.view.ViewParent
import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

private const val PRODUCT_NAME_CHARS =
    "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ" +
        "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ " +
        "-"

fun EditText.setupProductNameField() {
    keyListener = DigitsKeyListener.getInstance(PRODUCT_NAME_CHARS)
    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
    filters = emptyArray()
    setOnFocusChangeListener { _, hasFocus ->
        if (hasFocus) setFieldError(null)
    }
    clearErrorOnInput()
}

fun TextInputEditText.setupDecimalField() {
    inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
    filters = emptyArray()
    setOnFocusChangeListener { _, hasFocus ->
        if (hasFocus) setFieldError(null)
    }
    clearErrorOnInput()
}

fun EditText.findTextInputLayout(): TextInputLayout? {
    var parent: ViewParent? = parent
    while (parent != null) {
        if (parent is TextInputLayout) return parent
        parent = parent.parent
    }
    return null
}

fun EditText.setFieldError(message: String?) {
    val layout = findTextInputLayout() ?: return
    layout.isErrorEnabled = message != null
    layout.error = message
}

fun EditText.clearErrorOnInput() {
    addTextChangedListener(object : android.text.TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        override fun afterTextChanged(s: android.text.Editable?) {
            setFieldError(null)
        }
    })
}
