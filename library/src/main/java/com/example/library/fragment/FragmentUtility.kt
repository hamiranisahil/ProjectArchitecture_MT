package com.example.library.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.library.R
import com.example.library.util.hideKeyboard
import com.example.library.util.printLog


class FragmentUtility(val context: Context) {

    companion object {
        var fragmentLifecycleCallbacks: FragmentManager.FragmentLifecycleCallbacks? = null
        var fragmentBackListener: FragmnetBackListener? = null
    }

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
        bundle: Bundle? = null,
        customFragmentTag: String? = null
    ) {
        //set bundle data to fragments
        if (bundle != null) {
            fragment.arguments = bundle
        }

        val currentFragment = getCurrentFragment(containerId)
        val fragmentTag = fragment.javaClass.simpleName

        if (currentFragment != null) {
            if (customFragmentTag == null && currentFragment.tag == fragmentTag) {
//            if (currentFragment == fragment) {
                return
            } else {
                beginTransaction?.hide(currentFragment)
            }
        }

        when {
            isAdd -> {
                beginTransaction?.add(containerId, fragment, fragment.javaClass.simpleName)
            }
            isReplace -> {
                beginTransaction!!.replace(containerId, fragment, fragment.javaClass.simpleName)
            }
            else -> {
                printLog(TAG, context.getString(R.string.push_fragment_issue))
            }
        }
        if (isAddToBackStack) {
            beginTransaction!!.addToBackStack(fragment.javaClass.simpleName)
        }
        (context as Activity).hideKeyboard()
        beginTransaction?.commit()
        if (fragmentLifecycleCallbacks != null) {
            fragmentManager.registerFragmentLifecycleCallbacks(fragmentLifecycleCallbacks!!, true)
        }
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

    fun hideFragment(fragment: Fragment) {
        beginTransaction?.hide(fragment)?.commit()
    }

    fun showFragment(fragment: Fragment) {
        beginTransaction?.show(fragment)?.commit()
    }

    fun popFragment() {
        fragmentManager.popBackStack()
    }

    fun clearBackStack() {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    fun setFragmentListener(fragmentLifecycleCallbacks: FragmentManager.FragmentLifecycleCallbacks) {
        Companion.fragmentLifecycleCallbacks = fragmentLifecycleCallbacks
    }

    fun setFragmentOnBackListener(fragmentBackListener: FragmnetBackListener?) {
        Companion.fragmentBackListener = fragmentBackListener
    }

    interface FragmnetBackListener {
        fun onBackFragment()
    }

    fun pushChildFragment(
        parentFragment: Fragment,
        containerId: Int,
        fragment: Fragment,
        isAdd: Boolean,
        isReplace: Boolean = false,
        isAddToBackStack: Boolean = false,
        bundle: Bundle? = null,
        isAnimation: Boolean = false
    ) {
        //set bundle data to fragments
        if (bundle != null) {
            fragment.arguments = bundle
        }

        val childFragmentManager = parentFragment.childFragmentManager

        val childFragmentTransaction = childFragmentManager.beginTransaction()
        val fragmentTag = fragment.javaClass.simpleName

        if (parentFragment.tag == fragmentTag) {
            return
        }

        /*if (isAnimation) {
            childFragmentTransaction.setCustomAnimations(
                R.anim.slide_in_up,
                R.anim.slide_down
            )
        }*/

        when {
            isAdd -> {
                childFragmentTransaction.add(containerId, fragment, fragment.javaClass.simpleName)
            }
            isReplace -> {
                childFragmentTransaction.replace(
                    containerId,
                    fragment,
                    fragment.javaClass.simpleName
                )
            }
            else -> {
                printLog(TAG, context.getString(R.string.push_fragment_issue))
            }
        }
        if (isAddToBackStack) {
            childFragmentTransaction.addToBackStack(fragment.javaClass.simpleName)
        }
        (context as Activity).hideKeyboard()
        childFragmentTransaction.commitAllowingStateLoss()
    }

    fun getCurrentChildFragment(parentFragment: Fragment, containerId: Int): Fragment? {
        return parentFragment.childFragmentManager.findFragmentById(containerId)
    }

    fun getChildBackStackCount(parentFragment: Fragment): Int {
        return parentFragment.childFragmentManager.backStackEntryCount
    }

    fun hideChildFragment(parentFragment: Fragment, fragment: Fragment) {
        parentFragment.childFragmentManager.beginTransaction().hide(fragment)
            .commitAllowingStateLoss()
    }

    fun showChildFragment(parentFragment: Fragment, fragment: Fragment) {
        parentFragment.childFragmentManager.beginTransaction().show(fragment)
            .commitAllowingStateLoss()
    }

    fun popChildFragment(parentFragment: Fragment) {
//        try {
        parentFragment.childFragmentManager.popBackStackImmediate()
//        } catch (ignored: IllegalStateException) {
//        }
    }

    fun popChildFragmentAsync(parentFragment: Fragment) {
        parentFragment.childFragmentManager.popBackStack()
    }

}