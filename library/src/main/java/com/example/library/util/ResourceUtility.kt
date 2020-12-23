package com.example.library.util

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat

fun Context.getResColor(color: Int): Int {
    return ContextCompat.getColor(this, color)
}

fun Context.getResDrawable(drawable: Int): Drawable? {
    return ContextCompat.getDrawable(this, drawable)
}
