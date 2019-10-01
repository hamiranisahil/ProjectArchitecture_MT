package com.mt.library.util

import android.util.Log
import com.mt.library.BuildConfig

class MyLog {

    fun printLog(tag: String, message: String) {
        if (BuildConfig.DEBUG)
            Log.d(tag, message)
    }

}