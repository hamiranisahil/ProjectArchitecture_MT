package com.example.library.util

import android.util.Log
import com.example.library.BuildConfig


fun printLog(tag: String, message: String?) {
    if (BuildConfig.DEBUG)
        message?.let { Log.e(tag, it) }
}