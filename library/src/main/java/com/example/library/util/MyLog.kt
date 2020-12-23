package com.example.library.util

import android.app.Activity
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.example.library.BuildConfig


fun printLog(tag: String, message: String) {
    if (BuildConfig.DEBUG)
        Log.d(tag, message)
}

fun Activity.printLog(message: String) {
    if (BuildConfig.DEBUG)
        printLog(this.javaClass.simpleName, message)
}

fun Fragment.printLog(message: String) {
    if (BuildConfig.DEBUG)
        printLog(this.javaClass.simpleName, message)
}

fun ViewModel.printLog(message: String) {
    if (BuildConfig.DEBUG)
        printLog(this.javaClass.simpleName, message)
}