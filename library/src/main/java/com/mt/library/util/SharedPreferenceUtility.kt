package com.mt.library.util

import android.content.Context

class SharedPreferenceUtility {

    companion object {
        var SHARED_PREFERENCE_NAME = ""
    }

    fun setData(context: Context, key: String, value: Any) {
        when (value) {
            is String -> context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0).edit().putString(key, value).apply()
            is Int -> context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0).edit().putInt(key, value).apply()
            is Long -> context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0).edit().putLong(key, value).apply()
            is Float -> context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0).edit().putFloat(key, value).apply()
            is Boolean -> context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0).edit().putBoolean(key, value).apply()
            else -> MyLog().printLog("SharedPreferenceUtility", "Value Type is Not Allowed")
        }

    }

    fun getData(context: Context, key: String, defValue: Any): Any {
        return when (defValue) {
            is Int -> context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0).getInt(key, defValue)
            is Long -> context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0).getLong(key, defValue)
            is Float -> context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0).getFloat(key, defValue)
            is Boolean -> context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0).getBoolean(key, defValue)
            else -> context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0).getString(key, defValue.toString())!!
        }
    }
}