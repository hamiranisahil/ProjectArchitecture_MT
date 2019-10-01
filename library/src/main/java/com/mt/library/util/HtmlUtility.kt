package com.mt.library.util

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned

class HtmlUtility(val context: Context) {

    @Suppress("DEPRECATION")
    fun fromHtml(text: String): Spanned? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
        } else {
            return Html.fromHtml(text)
        }
    }

    fun setColor(color: Int, text: String): String {
        return "<font color=$color>${text}</font>"
    }

}