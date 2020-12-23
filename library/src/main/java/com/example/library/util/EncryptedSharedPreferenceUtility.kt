package com.example.library.util

import android.content.Context

class EncryptedSharedPreferenceUtility(context: Context) {

    /*companion object {
        var SHARED_PREFERENCE_NAME = ""
        var sharedPreferences: SharedPreferences? = null
    }

    init {
        if (sharedPreferences == null) {
            val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
            sharedPreferences = EncryptedSharedPreferences.create(
                SHARED_PREFERENCE_NAME,
                masterKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }
    }

    fun setData(context: Context, key: String, value: Any) {
        when (value) {
            is String -> sharedPreferences?.edit()?.putString(key, value)?.apply()
            is Int -> sharedPreferences?.edit()?.putInt(key, value)?.apply()
            is Long -> sharedPreferences?.edit()?.putLong(key, value)?.apply()
            is Float -> sharedPreferences?.edit()?.putFloat(key, value)?.apply()
            is Boolean -> sharedPreferences?.edit()?.putBoolean(key, value)?.apply()
            else -> printLog("SharedPreferenceUtility", "Value Type is Not Allowed")
        }

    }

    fun getData(context: Context, key: String, defValue: Any): Any {
        return when (defValue) {
            is Int -> requireNotNull(sharedPreferences?.getInt(key, defValue))
            is Long -> requireNotNull(sharedPreferences?.getLong(key, defValue))
            is Float -> requireNotNull(sharedPreferences?.getFloat(key, defValue))
            is Boolean -> requireNotNull(sharedPreferences?.getBoolean(key, defValue))
            else -> requireNotNull(sharedPreferences?.getString(key, defValue.toString())!!)
        }
    }*/
}