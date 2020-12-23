package com.example.library.util

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager.NameNotFoundException
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import java.lang.reflect.Method


fun Context.getDeviceUniqueId(): String? {
    return Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
}


fun getDeviceName(): String? {
    val manufacturer = Build.MANUFACTURER
    return capitalize(manufacturer)

}

fun getDeviceModel(): String? {
    val manufacturer = Build.MANUFACTURER
    val model = Build.MODEL
    return if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
        capitalize(model)
    } else {
        capitalize(manufacturer) + " " + model
    }
}


fun capitalize(s: String?): String {
    if (s == null || s.length == 0) {
        return ""
    }
    val first = s[0]
    return if (Character.isUpperCase(first)) {
        s
    } else {
        Character.toUpperCase(first).toString() + s.substring(1)
    }
}


fun Context.getBuildVersionName(): String {
    var version = "1.0"
    try {
        val pInfo: PackageInfo = this.getPackageManager().getPackageInfo(packageName, 0)
        version = pInfo.versionName
    } catch (e: NameNotFoundException) {
        e.printStackTrace()
    }
    return version
}


fun Context.getBuildVersionCode(): Int {
    var versionCode = 1
    try {
        val pInfo: PackageInfo = this.getPackageManager().getPackageInfo(packageName, 0)

        versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            pInfo.longVersionCode.toInt() // avoid huge version numbers and you will be ok
        } else {
            pInfo.versionCode
        }
    } catch (e: NameNotFoundException) {
        e.printStackTrace()
    }
    return versionCode
}

/**
 * Returns `null` if this couldn't be determined.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
@SuppressLint("PrivateApi")
fun hasNavigationBar(): Boolean {
    return try {
        val serviceManager =
            Class.forName("android.os.ServiceManager")
        val serviceBinder =
            serviceManager.getMethod("getService", String::class.java)
                .invoke(serviceManager, "window") as IBinder
        val stub =
            Class.forName("android.view.IWindowManager\$Stub")
        val windowManagerService =
            stub.getMethod("asInterface", IBinder::class.java).invoke(stub, serviceBinder)
        val hasNavigationBar: Method =
            windowManagerService.javaClass.getMethod("hasNavigationBar")
        hasNavigationBar.invoke(windowManagerService) as Boolean
    } catch (e: ClassNotFoundException) {
        Log.w("YOUR_TAG_HERE", "Couldn't determine whether the device has a navigation bar", e)
        true
    }
}


fun Activity.getNavigationBarHeight(): Int {
    val metrics = DisplayMetrics()
    this?.windowManager?.defaultDisplay?.getMetrics(metrics)
    val usableHeight = metrics.heightPixels
    this.windowManager?.defaultDisplay?.getRealMetrics(metrics)
    val realHeight = metrics.heightPixels
    return if (realHeight > usableHeight) realHeight - usableHeight else 0
}