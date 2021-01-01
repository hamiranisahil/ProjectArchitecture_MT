package com.example.library.util

import android.app.Activity
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel


fun printLog(tag: String, message: String?) {
    message?.let {
        Log.d(tag, it)
    }
}

fun Activity.printLog(message: String?) {
    printLog(this.javaClass.simpleName, message)
}

fun Fragment.printLog(message: String?) {
    printLog(this.javaClass.simpleName, message)
}

fun ViewModel.printLog(message: String?) {
    printLog(this.javaClass.simpleName, message)
}