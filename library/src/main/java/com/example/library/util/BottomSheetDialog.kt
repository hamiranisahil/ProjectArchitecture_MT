package com.example.library.util

import android.content.Context
import android.view.View
import com.example.library.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

fun Context.showBottomSheetDialog(
    layoutView: View,
    isCancelable: Boolean = false
): BottomSheetDialog {
    val dialog = BottomSheetDialog(this, R.style.bottomSheet)
    dialog.setContentView(layoutView)
    dialog.setCancelable(isCancelable)
    val mBehavior = BottomSheetBehavior.from(layoutView.parent as View)
    mBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    mBehavior.isHideable = true
    /*mBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

        override fun onSlide(p0: View, p1: Float) {

        }

        override fun onStateChanged(view: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                mBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    })*/
    dialog.show()
    return dialog
}


fun Context.showBottomSheetNotCancellable(
    layoutView: View,
    isCancelable: Boolean = false
): BottomSheetDialog {
    val dialog = BottomSheetDialog(this, R.style.bottomSheet)
    dialog.setContentView(layoutView)
    dialog.setCancelable(isCancelable)
    dialog.setCanceledOnTouchOutside(false)
    val mBehavior = BottomSheetBehavior.from(layoutView.parent as View)
    mBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    mBehavior.isHideable = false
    mBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

        override fun onSlide(p0: View, p1: Float) {

        }

        override fun onStateChanged(view: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                mBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    })
    dialog.show()
    return dialog
}

fun Context.showBottomSheetDialogSoftInput(
    layoutView: View,
    isCancelable: Boolean = false
): BottomSheetDialog {
    val dialog = BottomSheetDialog(this, R.style.bottomSheetResize)
    dialog.setContentView(layoutView)
    dialog.setCancelable(isCancelable)
    val mBehavior = BottomSheetBehavior.from(layoutView.parent as View)
    mBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    mBehavior.isHideable = true
    /*mBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

        override fun onSlide(p0: View, p1: Float) {

        }

        override fun onStateChanged(view: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                mBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    })*/
    dialog.show()
    return dialog
}