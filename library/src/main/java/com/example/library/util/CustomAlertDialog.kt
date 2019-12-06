package com.example.library.util

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.example.library.util.CommonClass.Companion.ALERT_DIALOG_SHOWN

fun Context.showDialogWithOneButton(
    title: String,
    message: String,
    buttonLabel: String,
    alertButtonClickListener: AlertButtonClickListener,
    isCancelable: Boolean = false
    ) {
    try {
        if (!ALERT_DIALOG_SHOWN) {
            val builder = AlertDialog.Builder(this).setTitle(title).setMessage(message)
            builder.setCancelable(isCancelable)
            builder.setPositiveButton(buttonLabel) { dialog, which ->
                ALERT_DIALOG_SHOWN = false
                alertButtonClickListener.onAlertClick(
                    dialog,
                    which
                )
            }
            builder.create().show()
            ALERT_DIALOG_SHOWN = true
        }
    } catch (e: Exception){
        e.printStackTrace()
    }
}

fun Context.showDialogWithTwoButton(
    title: String,
    message: String,
    positiveButtonLabel: String,
    negativeButtonLabel: String,
    alertTwoButtonClickListener: AlertTwoButtonClickListener,
    isCancelable: Boolean = false
    ) {
    if(!ALERT_DIALOG_SHOWN) {
        val builder = AlertDialog.Builder(this).setTitle(setHtmlText(title))
            .setMessage(setHtmlText(message)).setCancelable(isCancelable)

        builder.setPositiveButton(positiveButtonLabel) { dialog, which ->
            ALERT_DIALOG_SHOWN = false
            alertTwoButtonClickListener.onAlertClick(
                dialog,
                which,
                true
            )
        }
        builder.setNegativeButton(negativeButtonLabel) { dialog, which ->
            ALERT_DIALOG_SHOWN = false
            alertTwoButtonClickListener.onAlertClick(
                dialog,
                which,
                false
            )
        }
        builder.create().show()
        ALERT_DIALOG_SHOWN = true
    }
}

interface AlertButtonClickListener {
    fun onAlertClick(dialog: DialogInterface, which: Int)
}

interface AlertTwoButtonClickListener {
    fun onAlertClick(dialog: DialogInterface, which: Int, isPositive: Boolean)
}
