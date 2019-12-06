package com.example.library.util

import android.content.Context
import android.provider.Settings

fun Context.getDeviceUniqueId(): String? {
    return Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
}