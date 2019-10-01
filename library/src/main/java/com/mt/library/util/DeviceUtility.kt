package com.mt.library.util

import android.content.Context
import android.provider.Settings

class DeviceUtility {

    fun getDeviceUniqueId(context: Context): String? {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

}