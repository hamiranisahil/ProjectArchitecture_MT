package com.mt.library.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ActivityUtility {

    val DEFAULT_REQUEST_CODE = 12896

    fun openActivity(from: Context, to: Class<out AppCompatActivity>, bundle: Bundle?, finish: Finish) {
        val intent = Intent(from, to)
        if (bundle != null)
            intent.putExtras(bundle)
        from.startActivity(intent)

        if (finish == Finish.CLOSE_PREVIOUS)
            (from as AppCompatActivity).finish()
        else if (finish == Finish.CLOSE_ALL_PREVIOUS)
            (from as Activity).finishAffinity()

    }

    fun openActivity(
        from: Context,
        to: Class<out AppCompatActivity>,
        bundle: Bundle?,
        finish: Finish,
        setResult: Boolean = false,
        requestCode: Int = DEFAULT_REQUEST_CODE
    ) {
        val intent = Intent(from, to)
        if (bundle != null)
            intent.putExtras(bundle)
        if (setResult) {
            (from as Activity).startActivityForResult(intent, requestCode)
        } else {
            from.startActivity(intent)
        }

        if (finish == Finish.CLOSE_PREVIOUS)
            (from as AppCompatActivity).finish()
        else if (finish == Finish.CLOSE_ALL_PREVIOUS)
            (from as Activity).finishAffinity()
    }

    enum class Finish {
        CLOSE_PREVIOUS, CLOSE_ALL_PREVIOUS, NO_CLOSE
    }

}
