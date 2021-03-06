package com.example.library.util

// showDecimalValue:  = 2 means 123.23456 it will shows the 123.23.
fun Double.manageDecimalValue(showDecimalValue: Int): String {
    return String.format("%.${showDecimalValue}f", this)
}