package com.example.myarchitecture

import android.content.Context
import android.os.Environment
import com.example.library.api_call.ApiCall
import com.example.library.pusher.PusherUtility
import com.example.library.rx_api_call.RXApiCall
import com.example.library.util.SharedPreferenceUtility

class AppConfig {

    fun projectSetUp(context: Context) {

        SharedPreferenceUtility.SHARED_PREFERENCE_NAME = context.packageName

//        PusherUtility.CLUSTER = "mt1"
//        PusherUtility.APP_KEY = if (buildVariant.equals(BUILD_DEVELOPMENT)) "fb1e178363ba549af6f2" else if (buildVariant.equals(
//                        BUILD_STAGING
//                )
//        ) "e353e7529bc3ad5266bd" else ""

        val apiUrl = "https://min-api.cryptocompare.com/data/all/"
        RXApiCall.retrofit = null // we need to set null because again set BASE_URL
        RXApiCall.BASE_URL = apiUrl
        IMAGE_PREFIX_URL = apiUrl + "public/upload/"

        RXApiCall.HEADER_MAP = getHeader(context)
        RXApiCall.LOADING_TITLE =
                "Loading" // set Loading Title when api call shows the loading dialog.
        RXApiCall.LOADING_DIALOG_SHOW =
                true // true or false:  if you want to show loading dialog when api calling
        RXApiCall.INTERNET_DIALOG_SHOW =
                true // true or false:  when internet goes and user tries to api call then if you want to show no internet dialog.
        RXApiCall.HANDLE_STATUS =
                true // true or false: if you want to handle status automatically then set true or else pass false it gives directly response.
        RXApiCall.FILE_DOWNLOAD_PATH = Environment.getExternalStorageDirectory()
                .path // when we downloading file then set file path to save file at particular location.

        //map.put("ClassName", arrayOf("URL","METHOD","REQUEST CODE"))

        map["CoinListReq"] = arrayOf("coinlist", ApiCall.RequestType.GET, 1)
    }


    private fun getHeader(context: Context): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        return map
    }

    fun getRequestparams(classObject: Any): Array<Any> {
        val enclosingClass = classObject.javaClass
        val className = enclosingClass.simpleName
        return map[className]!!
    }

    companion object {
        @kotlin.jvm.JvmField
        var IMAGE_PREFIX_URL = ""
        private val map = HashMap<String, Array<Any>>()
        var tokenData: String = ""
        var API_VERSION = "v9"
    }

}