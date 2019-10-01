package com.mt.library.dialog

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.view.WindowManager

class CustomDialog(val context: Context) {

    private var themeResId: Int = -1

    constructor(context: Context, themeResId: Int) : this(context) {
        this.themeResId = themeResId
    }

    fun showDialog(layoutId: Int, isCancelable: Boolean, width: Int, height: Int): Dialog {
        val dialog = if (themeResId != -1) Dialog(context, themeResId) else Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) //before
        dialog.setContentView(layoutId)
        dialog.setCancelable(isCancelable)
        dialog.show()
        if (dialog.window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window!!.attributes)
            layoutParams.width = width
            layoutParams.height = height
            dialog.window!!.attributes = layoutParams
        }
        return dialog
    }

}