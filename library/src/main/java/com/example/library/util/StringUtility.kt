package com.example.library.util


fun getStringFromStartToCharIndex(char: Char, string: String): String {
    return string.substring(0, string.indexOf(char))
}

fun convertToCamelCase(text: String?): String? {
    if (text == null || text.isEmpty()) {
        return text
    }
    val converted = StringBuilder()
    var convertNext = true
    for (ch in text.toCharArray()) {
        var char = ch
        if (Character.isSpaceChar(char)) {
            convertNext = true
        } else if (convertNext) {
            char = Character.toTitleCase(char)
            convertNext = false
        } else {
            char = Character.toLowerCase(char)
        }
        converted.append(char)
    }
    return converted.toString()
}