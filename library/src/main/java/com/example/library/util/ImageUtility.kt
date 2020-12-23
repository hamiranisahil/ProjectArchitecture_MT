package com.example.library.util

import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.Base64
import android.view.View
import java.io.ByteArrayOutputStream
import java.io.File

fun bitmapToBase64(bitmap: Bitmap): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
}

fun base64ToBitmap(string: String): Bitmap {
    val byte = Base64.decode(string, 0)
    return BitmapFactory.decodeByteArray(byte, 0, byte.size)
}

fun getBitmapFromDrawable(drawable: Drawable): Bitmap {
    val bmp = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bmp)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bmp
}

fun getBitmapFromFilePath(path: String): Bitmap? {
    return BitmapFactory.decodeFile(File(path).absolutePath)
}

fun getBitmapFromView(view: View): Bitmap? {
    view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    var bitmap =
        Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    var canvas = Canvas(bitmap)
    view.draw(canvas)
    return bitmap
}

private fun getMarkerBitmapFromView(view: View): Bitmap {
    view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    val returnedBitmap = Bitmap.createBitmap(
        view.measuredWidth, view.measuredHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(returnedBitmap)
    canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
    view.background?.draw(canvas)
    view.draw(canvas)
    return returnedBitmap
}
