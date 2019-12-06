package com.example.library.util

fun getStringFromStartToCharIndex(char: Char, string: String): String {
    return string.substring(0, string.indexOf(char))
}