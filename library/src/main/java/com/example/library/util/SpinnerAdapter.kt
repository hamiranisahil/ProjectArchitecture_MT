package com.example.library.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Spinner

class SpinnerAdapter(
    val context: Context,
    var layout: Int,
    val list: MutableList<Any>,
    val onSpinnerViewListener: OnSpinnerViewListener,
    val spinner: Spinner? = null
) : BaseAdapter() {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: mInflater.inflate(layout, parent, false)
        view.setPadding(0, 0, 0, 0)
        onSpinnerViewListener.onSpinnerViewReady(position, view, parent, spinner)
        return view
    }

    override fun getItem(p0: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return -1
    }

    override fun getCount(): Int {
        return list.count()
    }

    interface OnSpinnerViewListener {
        fun onSpinnerViewReady(
            position: Int,
            convertView: View?,
            parent: ViewGroup?,
            spinner: Spinner?
        )
    }

}