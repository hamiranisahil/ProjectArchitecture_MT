package com.mt.library.util

import android.content.Context
import android.widget.Toast

class MyToast(val context: Context) {

    fun show(message: String){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

}