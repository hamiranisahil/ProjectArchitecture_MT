package com.example.library.util

import android.app.Activity
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup

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
