package com.mt.library.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(
        val context: Context,
        val layout: Int,
        val list: MutableList<*>,
        val onBindViewHolderListener: OnBindViewHolderListener
) : RecyclerView.Adapter<RecyclerViewAdapter.CommonViewHolder>() {

    var onViewTypeListener: OnViewTypeListener? = null

    constructor(context: Context, list: MutableList<*>, onBindViewHolderListener: OnBindViewHolderListener, onViewTypeListener: OnViewTypeListener) : this(context, -1, list, onBindViewHolderListener) {
        this.onViewTypeListener = onViewTypeListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CommonViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(if (onViewTypeListener != null) onViewTypeListener!!.getLayout(viewType) else layout, viewGroup, false)
        return CommonViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return if (onViewTypeListener != null) onViewTypeListener!!.getItemViewType(position) else position
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(viewHolder: CommonViewHolder, position: Int) {
        onBindViewHolderListener.onViewBind(viewHolder, position)
    }

    class CommonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface OnBindViewHolderListener {
        fun onViewBind(viewHolder: CommonViewHolder, position: Int)
    }

    interface OnViewTypeListener {
        fun getItemViewType(position: Int): Int
        fun getLayout(viewType: Int): Int
    }

}