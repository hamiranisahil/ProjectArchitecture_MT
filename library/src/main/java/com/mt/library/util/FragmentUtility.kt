package com.mt.library.util

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class FragmentUtility(context: Context) {

        var fragmentManager: FragmentManager? = null
        var beginTransaction: FragmentTransaction? = null


    init {
            fragmentManager = (context as AppCompatActivity).supportFragmentManager
            beginTransaction = fragmentManager!!.beginTransaction()
    }

    fun replaceFragment(context: Context, containerId: Int, fragment: Fragment, needBackFragment: Boolean) {
        beginTransaction!!.replace(containerId, fragment)
        if (needBackFragment) {
            beginTransaction!!.addToBackStack(fragment.toString())
        }
        beginTransaction!!.commit()
    }

    fun getCountBackStack(): Int {
        return fragmentManager!!.backStackEntryCount
    }

    fun goBackFragment() {
        if (fragmentManager!!.backStackEntryCount > 0) {
            fragmentManager!!.popBackStack()
        }
    }

}