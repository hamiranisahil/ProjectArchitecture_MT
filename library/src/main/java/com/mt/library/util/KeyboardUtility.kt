package com.mt.library.util

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.InputMethodManager


class KeyboardUtility {

    interface OnKeyboardChangeEventListener {
        fun onKeyboardChange(isVisible: Boolean)
    }

    fun showKeyboard(view: View, context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (!imm.isActive)
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    fun hideKeyboard(activity: Activity): Boolean {
        // Check if no view has focus:
        var keyboardClosed = false
        try {
            val view = activity.currentFocus
            if (view != null) {
                val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
                keyboardClosed = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return keyboardClosed
    }

    companion object {

        fun eventOpenClose(
            context: Context,
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
    }
}
