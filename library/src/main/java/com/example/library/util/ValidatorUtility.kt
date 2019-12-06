package com.example.library.util

import android.util.Patterns
import android.widget.EditText

fun EditText.getTextFromEditText(): String {
    return text.toString().trim()
}

fun EditText.isEmpty(): Boolean {
    return getTextFromEditText().isEmpty()
}

fun EditText.isValidMobileNumber(): Boolean {
    return getTextFromEditText().isValidMobileNumber()
}

fun String.isValidMobileNumber(): Boolean {
    return Patterns.PHONE.matcher(this).matches()
}

fun EditText.isValidEmailAddress(): Boolean {
    return getTextFromEditText().isValidEmailAddress()
}

fun String.isValidEmailAddress(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun EditText.isValidMin(min: Int): Boolean {
    return getTextFromEditText().isValidMin(min)
}

fun String.isValidMin(min: Int): Boolean {
    return length >= min
}

fun EditText.isValidMax(max: Int): Boolean {
    return getTextFromEditText().isValidMax(max)
}

fun String.isValidMax(max: Int): Boolean {
    return length <= max
}

fun isValidMatch(editText1: EditText, editText2: EditText): Boolean {
    return isValidMatch(editText1.text.toString(), editText2.text.toString())
}

fun isValidMatch(string1: String, string2: String): Boolean {
    return string1 == string2
}

fun EditText.isValidMinMax(min: Int, max: Int): Boolean {
    return getTextFromEditText().isValidMinMax(min, max)
}

fun String.isValidMinMax(min: Int, max: Int): Boolean {
    return isValidMin(min) && isValidMax(max)
}
