package com.mt.library.gson

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class GsonConvert<T>(val response: String) {

    fun typeToken(): T {
        val typeToken = object : TypeToken<T>() {}
        return Gson().fromJson<T>(response, typeToken.type) as T
    }
}