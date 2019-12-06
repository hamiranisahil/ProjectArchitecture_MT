package com.example.library.util

import android.os.Build
import android.text.Html
import android.text.Spanned


@Suppress("DEPRECATION")
fun setHtmlText(text: String): Spanned? {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
    } else {
        return Html.fromHtml(text)
    }
}

fun setHtmlColor(color: Int, text: String): String {
    return "<font color=$color>${text}</font>"
}