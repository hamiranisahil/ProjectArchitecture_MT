package com.example.library.util

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.example.library.util.CommonClass.Companion.alertDialog

fun Context.showDialogWithOneButton(
    title: String,
    message: String?,
    buttonLabel: String,
    alertButtonClickListener: AlertButtonClickListener,
    isCancelable: Boolean = false
) {
    try {
        if (alertDialog == null || (alertDialog != null && !requireNotNull(alertDialog?.isShowing))) {
            val builder =
                AlertDialog.Builder(this).setPositiveButton(buttonLabel, null).setTitle(title)
                    .setMessage(message).setCancelable(isCancelable)
            alertDialog = builder.create()

            alertDialog?.setOnShowListener { dialog ->
                val button = (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
                button.setOnClickListener {
                    alertButtonClickListener.onAlertClick(
                        dialog,
                        AlertDialog.BUTTON_POSITIVE
                    )
                }
            }
            alertDialog?.show()
        }

    } catch (e: Exception) {
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
    try {
        if (alertDialog == null || (alertDialog != null && !requireNotNull(alertDialog?.isShowing))) {
            val builder = AlertDialog.Builder(this).setTitle(setHtmlText(title))
                .setMessage(setHtmlText(message)).setCancelable(isCancelable)

            builder.setPositiveButton(positiveButtonLabel) { dialog, which ->
                alertTwoButtonClickListener.onAlertClick(
                    dialog,
                    which,
                    true
                )
            }
            builder.setNegativeButton(negativeButtonLabel) { dialog, which ->
                alertTwoButtonClickListener.onAlertClick(
                    dialog,
                    which,
                    false
                )
            }
            builder.create().show()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

interface AlertButtonClickListener {
    fun onAlertClick(dialog: DialogInterface, which: Int)
}

interface AlertTwoButtonClickListener {
    fun onAlertClick(dialog: DialogInterface, which: Int, isPositive: Boolean)
}
