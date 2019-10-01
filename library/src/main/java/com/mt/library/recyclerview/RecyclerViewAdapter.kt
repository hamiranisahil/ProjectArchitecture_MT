package com.mt.library.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(
    val context: Context,
    val layout: Int,
    val list: List<Any>,
    val onBindViewHolderListener: OnBindViewHolderListener
) : RecyclerView.Adapter<RecyclerViewAdapter.CommonViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CommonViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(layout, viewGroup, false)
        return CommonViewHolder(view)
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

    interface OnClickListener {
        fun onItemClick(viewHolder: CommonViewHolder, position: Int)
    }
}