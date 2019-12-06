package com.example.library.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

const val DEFAULT_REQUEST_CODE = 12896

fun Context.openActivity(to: Class<out AppCompatActivity>, bundle: Bundle?, finish: Finish) {
    val intent = Intent(this, to)
    if (bundle != null)
        intent.putExtras(bundle)
    this.startActivity(intent)

    if (finish == Finish.CLOSE_PREVIOUS)
        (this as AppCompatActivity).finish()
    else if (finish == Finish.CLOSE_ALL_PREVIOUS)
        (this as Activity).finishAffinity()

}

fun Context.openActivity(
    to: Class<out AppCompatActivity>,
    bundle: Bundle?,
    finish: Finish,
    setResult: Boolean = false,
    requestCode: Int = DEFAULT_REQUEST_CODE
) {
    val intent = Intent(this, to)
    if (bundle != null)
        intent.putExtras(bundle)
    if (setResult) {
        (this as Activity).startActivityForResult(intent, requestCode)
    } else {
        this.startActivity(intent)
    }

    if (finish == Finish.CLOSE_PREVIOUS)
        (this as AppCompatActivity).finish()
    else if (finish == Finish.CLOSE_ALL_PREVIOUS)
        (this as Activity).finishAffinity()
}

fun Activity.setFullScreenActivity(){
    //set full screen display
    this.requestWindowFeature(Window.FEATURE_NO_TITLE)
    this.window.setFlags(
        WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN)
}

enum class Finish {
    CLOSE_PREVIOUS, CLOSE_ALL_PREVIOUS, NO_CLOSE
}
