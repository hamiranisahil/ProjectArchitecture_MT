package com.example.library.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.library.BuildConfig
import com.example.library.R

class IntentUtility(val context: Context) {

    fun openUrl(url: String?) {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))

        } catch (e: Exception){
            context.showToast(context.getString(R.string.no_application_to_handle_this_request))
        }
    }

    fun openDialer(context: Context, number: String){
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$number")
        context.startActivity(intent)
    }

    fun launchPlayStoreWithPackageName(packageName: String) {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)))

        } catch (e: Exception) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)
                )
            )
        }
    }

}