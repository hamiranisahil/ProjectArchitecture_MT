package com.example.library.util

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.InputMethodManager


fun Context.showKeyboard() {
    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

    /*val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (!imm.isActive)
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)*/
}

fun Context.hideKeyboard(): Boolean {
    // Check if no view has focus:
    var keyboardClosed = false
    try {
        val view = (this as Activity).currentFocus
        if (view != null) {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            keyboardClosed = true
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return keyboardClosed
}

fun Activity.hideImplicitKeyboard() {
    // Check if no view has focus:
    try {
        val imm: InputMethodManager? =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.eventOpenClose(
    view: View, onKeyboardChangeEventListener: OnKeyboardChangeEventListener?
) {
    view.viewTreeObserver.addOnGlobalLayoutListener {
        if (onKeyboardChangeEventListener != null) {
            val rect = Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val screenHeight = view.rootView.height
            val keypadHeight = screenHeight - rect.bottom

            if (keypadHeight > screenHeight * 0.15) {
                onKeyboardChangeEventListener.onKeyboardChange(true)
            } else {
                onKeyboardChangeEventListener.onKeyboardChange(false)
            }
        }
    }
}


interface OnKeyboardChangeEventListener {
    fun onKeyboardChange(isVisible: Boolean)
}