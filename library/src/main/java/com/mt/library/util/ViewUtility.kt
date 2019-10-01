package com.mt.library.util

import android.app.Activity
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup

class ViewUtility {

    fun getViewDistance(activity: Activity, view: View): Int {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val viewSize = IntArray(2)
        view.getLocationOnScreen(viewSize)
        return displayMetrics.heightPixels - viewSize[1]
    }

    fun enableDisableViewGroup(viewGroup: ViewGroup, isEnabled: Boolean){
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