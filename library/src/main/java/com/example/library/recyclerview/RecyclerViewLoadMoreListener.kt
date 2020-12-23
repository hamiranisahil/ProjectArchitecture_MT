package com.example.library.recyclerview

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerViewLoadMoreListener (val layoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if(!isLoading() && !isLastPage()){
            if((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >=0 ) {
                onLoadMoreItems()
            }
        }
    }

    abstract fun onLoadMoreItems()
    abstract fun getTotalPageCount()
    abstract fun isLastPage() : Boolean
    abstract fun isLoading() : Boolean

}