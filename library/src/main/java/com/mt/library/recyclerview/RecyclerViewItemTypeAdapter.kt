package com.mt.library.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewItemTypeAdapter(
    val context: Context,
    private val onBindViewHolderListener: OnBindViewHolderListener
) : RecyclerView.Adapter<RecyclerViewItemTypeAdapter.ItemHolder>() {

    val list: MutableList<Any> = ArrayList()

    override fun getItemViewType(position: Int): Int {
        return onBindViewHolderListener.getItemViewType(position, list[position])
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ItemHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(onBindViewHolderListener.getLayout(viewType), viewGroup, false)
        return ItemHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(viewHolder: ItemHolder, position: Int) {
        onBindViewHolderListener.onViewBind(viewHolder, position, list[position])
    }

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface OnBindViewHolderListener {
        fun getItemViewType(position: Int, item: Any): Int
        fun getLayout(viewType: Int): Int
        fun onViewBind(viewHolder: ItemHolder, position: Int, item: Any)
    }

    interface OnClickListener {
        fun onItemClick(viewHolder: ItemHolder, position: Int)
    }

    fun addItem(any: Any) {
        list.add(any)
        notifyItemInserted(list.size - 1)
    }

    fun addAllItem(list: List<Any>) {
        for (any in list) {
            addItem(any)
        }
    }

    fun removeItem() {
        list.clear()
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        if (position > -1) {
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun removeIem(any: Any) {
        removeItem(list.indexOf(any))
    }

    fun changeItem(position: Int, any: Any) {
        if (position > -1) {
            list[position] = any
            notifyItemChanged(position)
        }
    }

    fun getItem(position: Int): Any {
        return list[position]
    }
}