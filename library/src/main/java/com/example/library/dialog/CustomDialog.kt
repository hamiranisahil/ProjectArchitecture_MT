package com.example.library.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.*
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT

class CustomDialog(
    val context: Context,
    val onCancelListener: DialogInterface.OnCancelListener? = null
) {

    private var themeResId: Int = -1

    constructor(
        context: Context,
        themeResId: Int,
        onCancelListener: DialogInterface.OnCancelListener? = null
    ) : this(context, onCancelListener) {
        this.themeResId = themeResId
    }

    fun showDialog(view: View, isCancelable: Boolean, width: Int = ViewGroup.LayoutParams.MATCH_PARENT, height: Int = WRAP_CONTENT): Dialog {
        return this.showDialog(-1, null, isCancelable, width, height)
    }

    fun showDialog(layoutId: Int, isCancelable: Boolean, width: Int = ViewGroup.LayoutParams.MATCH_PARENT, height: Int = WRAP_CONTENT): Dialog {
        return this.showDialog(layoutId, null, isCancelable, width, height)
    }

    private fun showDialog(
        layoutId: Int,
        view: View?,
        isCancelable: Boolean,
        width: Int,
        height: Int
    ): Dialog {
        val dialog = if (themeResId != -1) Dialog(context, themeResId) else Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) //before
        if (view != null) {
            dialog.setContentView(view)
        } else {
            dialog.setContentView(layoutId)
        }
        dialog.setCancelable(isCancelable)
        dialog.show()

        if (dialog.window != null) {
            dialog.window?.setWindowAnimations(themeResId)
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window!!.attributes)
            layoutParams.width = width
            layoutParams.height = height
            dialog.window!!.attributes = layoutParams
        }
        dialog.setOnKeyListener { dialog, keyCode, event ->
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> {
//                    (context as Activity).onBackPressed()
                    onCancelListener?.onCancel(dialog)
                }
                else -> {
                    return@setOnKeyListener false
                }

            }
            return@setOnKeyListener true
        }
        return dialog
    }


}