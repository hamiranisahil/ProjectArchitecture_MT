package com.example.library.util

import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

private val delay = 500L
private var lastEditTime = 0L
private var handler = Handler()

fun EditText.userTypingListener(typingListener: TypingListener?) {
    val runnable = Runnable {
        run {
            if (System.currentTimeMillis() > (lastEditTime + delay - 500)) {
                typingListener?.typingStopped(this)
            }
        }
    }
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            lastEditTime = System.currentTimeMillis()
            try {
                handler.postDelayed(runnable, delay)
            } catch (e: Exception) {
                printLog("afterTextChanged", "${e.message}")
            }

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            try {
                handler.removeCallbacks(runnable)
            } catch (e: Exception) {
                printLog("onTextChanged", "${e.message}")
            }

        }

    })
}

interface TypingListener {
    fun typingStopped(editText: EditText)
}
