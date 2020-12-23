package com.example.library.util

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager

fun Activity.getViewDistance(view: View): Int {
    val displayMetrics = DisplayMetrics()
    this.windowManager.defaultDisplay.getMetrics(displayMetrics)
    val viewSize = IntArray(2)
    view.getLocationOnScreen(viewSize)
    return displayMetrics.heightPixels - viewSize[1]
}

fun enableDisableViewGroup(viewGroup: ViewGroup?, isEnabled: Boolean) {
    if (viewGroup != null) {
        val childCount = viewGroup.childCount
        for (i in 0 until childCount) {
            val view = viewGroup.getChildAt(i)
            view.isEnabled = isEnabled
            if (view is ViewGroup) {
                enableDisableViewGroup(view, isEnabled)
            }
        }
    }
}

fun getScreenWidth(context: Context): Int {
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val dm = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(dm)
    return dm.widthPixels
}

fun dpToPx(context: Context, value: Int): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        value.toFloat(),
        context.resources.displayMetrics
    ).toInt()
}
