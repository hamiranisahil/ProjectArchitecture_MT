package com.example.library

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import com.example.library.dialog.CustomDialog

class ProgressDialog() {

    fun showProgressDialog(context: Context) {
        if (progressDialog == null) {
            progressDialog = CustomDialog(
                context,
                R.style.full_screen_dialog
            ).showDialog(
                R.layout.loading_api_progress, false,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    fun dismissProgressDialog() {
        if (progressDialog != null) {
            if (progressDialog!!.isShowing) {
                progressDialog!!.dismiss()
            }
            progressDialog = null
        }
    }

    companion object {
        var progressDialog: Dialog? = null
    }
}