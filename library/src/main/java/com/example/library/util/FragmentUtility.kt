package com.example.library.util

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.library.R

class FragmentUtility(val context: Context) {

    var fragmentManager: FragmentManager
    var beginTransaction: FragmentTransaction? = null
    var TAG = FragmentUtility::class.java.simpleName

    init {
        fragmentManager = (context as AppCompatActivity).supportFragmentManager
        beginTransaction = fragmentManager.beginTransaction()
    }

    fun pushFragment(
        containerId: Int,
        fragment: Fragment,
        isAdd: Boolean,
        isReplace: Boolean = false,
        isAddToBackStack: Boolean = false,
        bundle: Bundle? = null
    ) {

        //set bundle data to fragments
        if (bundle != null) {
            fragment.setArguments(bundle)
        }

        val currentFragment = getCurrentFragment(containerId)
//        val fragmentTag = fragment.javaClass.simpleName



        if (currentFragment != null) {
//            if (currentFragment.tag == fragmentTag) {
            if (currentFragment == fragment) {
                return
            } else {
                beginTransaction?.hide(currentFragment)
            }
        }

        if (isAdd) {
            beginTransaction?.add(containerId, fragment, fragment.javaClass.simpleName)
        } else if (isReplace) {
            beginTransaction!!.replace(containerId, fragment, fragment.javaClass.simpleName)
        } else {
            printLog(TAG, context.getString(R.string.push_fragment_issue))
        }
        if (isAddToBackStack) {
            beginTransaction!!.addToBackStack(fragment.javaClass.simpleName)
        }
        (context as Activity).hideKeyboard()
        beginTransaction?.commit()
    }

    interface FragmentListener {
        fun onStackFragmentFinish(fragment: Fragment?, bundle: Bundle?)
    }

    fun getCurrentFragment(containerId: Int): Fragment? {
        return fragmentManager.findFragmentById(containerId)
    }

    fun getBackStackCount(): Int {
        return fragmentManager.backStackEntryCount
    }

}