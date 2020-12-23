package com.example.library.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(
    val context: Context,
    val layout: Int,
    val list: MutableList<*>,
    val onBindViewHolderListener: OnBindViewHolderListener,
    val recyclerView: RecyclerView? = null,
    val numberOfRows: Int = 0,
    val onNumberOfRowHeightMeasure: OnNumberOfRowHeightMeasure? = null
) : RecyclerView.Adapter<RecyclerViewAdapter.CommonViewHolder>() {

    var onViewTypeListener: OnViewTypeListener? = null
    var numberOfRowsHeight: Double = 0.0
    var isCalledOnce = false
    var isCalledCountMatchWithList = 0
//    var onRowHeightMeasure: OnNumberOfRowHeightMeasure? = null


    constructor(
        context: Context,
        list: MutableList<*>,
        onBindViewHolderListener: OnBindViewHolderListener,
        onViewTypeListener: OnViewTypeListener,
        recyclerView: RecyclerView? = null,
        numberOfRows: Int = 0,
        onNumberOfRowHeightMeasure: OnNumberOfRowHeightMeasure? = null
    ) : this(
        context,
        -1,
        list,
        onBindViewHolderListener,
        recyclerView,
        numberOfRows,
        onNumberOfRowHeightMeasure
    ) {
        this.onViewTypeListener = onViewTypeListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CommonViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(
            if (onViewTypeListener != null) onViewTypeListener!!.getLayout(
                viewType,
                recyclerView
            ) else layout, viewGroup, false
        )
        return CommonViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return if (onViewTypeListener != null) onViewTypeListener!!.getItemViewType(
            position,
            recyclerView
        ) else position
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(viewHolder: CommonViewHolder, position: Int) {
        onBindViewHolderListener.onViewBind(viewHolder, position, recyclerView)
        if (numberOfRows > 0) {
            if (list.size >= numberOfRows) {
                viewHolder.itemView.post {
                    if (numberOfRows > position && !isCalledOnce) {
                        numberOfRowsHeight += viewHolder.itemView.height
                    }
                    if ((numberOfRows - 1) == position && !isCalledOnce) {
                        onNumberOfRowHeightMeasure?.onRowHeightCount(numberOfRowsHeight)
                        isCalledOnce = true
                    }
                }
            } else {
                viewHolder.itemView.post {
                    if (!isCalledOnce) {
                        numberOfRowsHeight += viewHolder.itemView.height
                        onNumberOfRowHeightMeasure?.onRowHeightCount(numberOfRowsHeight)
                        isCalledCountMatchWithList+=1
                        if(isCalledCountMatchWithList==list.size){
                            isCalledOnce = true
                        }
                    }
                }
            }
        }
    }

    class CommonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface OnBindViewHolderListener {
        fun onViewBind(binding: CommonViewHolder, position: Int, recyclerView: RecyclerView?)
    }

    interface OnViewTypeListener {
        fun getItemViewType(position: Int, recyclerView: RecyclerView?): Int
        fun getLayout(viewType: Int, recyclerView: RecyclerView?): Int
    }

//    fun setNumberOfRows(rowsCount: Int, onNumberOfRowHeightMeasure: OnNumberOfRowHeightMeasure) {
//        this.numberOfRows = rowsCount
//        this.onRowHeightMeasure = onNumberOfRowHeightMeasure
//    }

    interface OnNumberOfRowHeightMeasure {
        fun onRowHeightCount(totalHeight: Double)
    }

}