package com.mt.library.util

class StringUtility {
    fun getStringFromChar(char: Char, string: String) : String {
        return string.substring(0, string.indexOf(char))
    }
}