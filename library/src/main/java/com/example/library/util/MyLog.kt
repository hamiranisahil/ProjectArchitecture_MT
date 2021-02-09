package com.example.library.util

import android.app.Activity
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

const val chunkSize = 2048

fun printLog(tag: String, message: String?) {
    message?.let {
        var i = 0
        while (i < it.length) {
            Log.d(tag, it.substring(i, Math.min(it.length, i + chunkSize)))
            i += chunkSize
        }
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