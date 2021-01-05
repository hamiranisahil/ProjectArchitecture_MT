package com.example.library.rxutil

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

fun EditText.fromView() : Observable<String> {
    val publishSubject = PublishSubject.create<String>()
    this.addTextChangedListener(object : TextWatcher{
        override fun afterTextChanged(s: Editable?) {
            publishSubject.onNext(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

    })
    return publishSubject
}