package com.example.library.api_call

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.StringDef
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.library.R
import com.example.library.dialog.CustomDialog
import com.example.library.modals.CommonRes
import com.example.library.modals.MultipartModal
import com.example.library.util.*
import com.google.android.gms.common.internal.service.Common
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.dialog_for_api.*
import kotlinx.android.synthetic.main.loading_api_progress.view.*
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.File
import java.net.SocketTimeoutException
import java.net.UnknownServiceException
import java.util.concurrent.TimeUnit

class ApiCall {

    lateinit var context: Context
    private lateinit var requestParams: Array<Any>
    lateinit var paramsBody: Any
    lateinit var webServiceType: String
    lateinit var retrofitResponseListener: RetrofitResponseListener

    internal var url = ""
    var method = ""
    var requestCode = -1
    private var rootView: View? = null
    private var responseCall: Call<ResponseBody>? = null
    private var apiInterface: ApiInterface
    var jsonString: String? = null


    constructor(
        context: Context,
        requestParams: Array<Any>,
        multipartModalList: ArrayList<MultipartModal>?,
        paramsBody: Any, @WebServiceType.Type webServiceType: String,
        retrofitResponseListener: RetrofitResponseListener
    ) {
        MULTIPART_MODAL_LIST = multipartModalList
        ApiCall(context, requestParams, paramsBody, webServiceType, retrofitResponseListener)
    }

    constructor(
        context: Context,
        requestParams: Array<Any>,
        paramsBody: Any, @WebServiceType.Type webServiceType: String,
        retrofitResponseListener: RetrofitResponseListener
    ) {
        this.context = context
        this.requestParams = requestParams
        this.paramsBody = paramsBody
        this.webServiceType = webServiceType
        this.retrofitResponseListener = retrofitResponseListener

        url = requestParams[0] as String
        method = requestParams[1] as String
        requestCode = requestParams[2] as Int
        LOADING_DIALOG_SHOW = if (requestParams.size > 3) requestParams[3] as Boolean else true

        rootView =
            (context as Activity).window.decorView.rootView.findViewById(android.R.id.content)

        handleNormalCall()
    }

    private fun handleNormalCall() {
        jsonString = Gson().toJson(paramsBody)

        when (method) {
            RequestType.GET -> {
                responseCall = if (paramsBody is String) {
                    apiInterface.get(HEADER_MAP!!, url + paramsBody, getMapFromGson(null))
                } else {
                    apiInterface.get(HEADER_MAP!!, url, getMapFromGson(jsonString))
                }
            }
            RequestType.POST -> {
                responseCall = apiInterface.postRaw(
                    HEADER_MAP!!,
                    url,
                    RequestBody.create(
                        MediaType.parse("application/json; charset=utf-8"),
                        jsonString!!
                    )
                )
            }
            RequestType.POST_FORM_DATA -> {
                val filesList = ArrayList<MultipartBody.Part>()
                val partMap = HashMap<String, RequestBody>()

                if (MULTIPART_MODAL_LIST != null) {
                    for (multipartModal in MULTIPART_MODAL_LIST!!) {
                        val file = File(context.cacheDir, File(multipartModal.filePath).name)
                        val part = MultipartBody.Part.createFormData(
                            multipartModal.fileKey,
                            file.name,
                            RequestBody.create(MediaType.parse("*/*"), file)
                        )
                        filesList.add(part)
                    }
                }

                val gsonMap = getMapFromGson(jsonString)
                for (map in gsonMap) {
                    partMap[map.key] =
                        RequestBody.create(MediaType.parse("text/plain"), map.value.toString())
                }
                responseCall = apiInterface.postFormData(HEADER_MAP!!, url, filesList, partMap)
            }
        }
        call()
    }

    init {
        apiInterface = client.create(ApiInterface::class.java)
        if (HEADER_MAP == null) {
            HEADER_MAP = HashMap()
        }
    }

    private val client: Retrofit
        get() {
            if (retrofit == null) {
                val okhttpBuilder = OkHttpClient.Builder()
                okhttpBuilder.connectTimeout(30, TimeUnit.SECONDS)
                okhttpBuilder.readTimeout(30, TimeUnit.SECONDS)
                okhttpBuilder.writeTimeout(30, TimeUnit.SECONDS)
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okhttpBuilder.build())
                    .build()
            }
            return retrofit!!
        }


    private fun call() {
        if (INTERNET_DIALOG_SHOW && context.isOnline()) {
            var progressView: View? = null
            var frameLayout: FrameLayout? = null
            if (LOADING_DIALOG_SHOW) {
                progressView =
                    LayoutInflater.from(context).inflate(R.layout.loading_api_progress, null)
                progressView.tvLoading.text = LOADING_TITLE

                rootView =
                    (context as Activity).window.decorView.rootView.findViewById(android.R.id.content)

                frameLayout = rootView as FrameLayout
                frameLayout.addView(progressView)
            }

            if (responseCall != null) {
                responseCall!!.clone().enqueue(object : Callback<ResponseBody> {

                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        try {

                            printLog(
                                "ApiCall - Request",
                                "Headers: ${response.raw().request().headers()} Url: ${response.raw().request().url()} Method: $method RequestCode: $requestCode WebServiceType: $webServiceType FileDownloadPath: $FILE_DOWNLOAD_PATH"
                            )
                            printLog("ApiCall - Request", "ParamsBody: $jsonString")


                            when (response.code()) {
                                STATUS_200 -> {

                                    var bodyString = ""
                                    var responseBody: ResponseBody? = null


                                    if (webServiceType == WebServiceType.WS_FILE_DOWNLOAD || webServiceType == WebServiceType.WS_FILE_DOWNLOAD_WITH_MESSAGE) {
                                        responseBody = response.body()
                                        if (responseBody == null) {
                                            retrofitResponseListener.onSuccess(null, requestCode)
                                            return
                                        }

                                    } else {
                                        bodyString = response.body()?.string()!!
                                    }

                                    printLog(
                                        "ApiCall - Response",
                                        "ParamsBody: $bodyString"
                                    )

                                    if (LOADING_DIALOG_SHOW) {
                                        frameLayout!!.removeView(progressView)
                                    }

                                    if (!HANDLE_STATUS) {
                                        retrofitResponseListener.onSuccess(
                                            "$FILE_DOWNLOAD_PATH/$paramsBody", requestCode
                                        )
                                        return
                                    }

                                    val commonRes =
                                        Gson().fromJson(bodyString, CommonRes::class.java)
                                    if (commonRes == null) {
                                        if (webServiceType == WebServiceType.WS_FILE_DOWNLOAD || webServiceType == WebServiceType.WS_FILE_DOWNLOAD_WITH_MESSAGE) {
                                            ApiFileDownloader(
                                                context,
                                                responseBody!!,
                                                paramsBody as String,
                                                requestCode,
                                                retrofitResponseListener
                                            )
                                            if (webServiceType == WebServiceType.WS_FILE_DOWNLOAD_WITH_MESSAGE) {
                                                context.showToast(context.getString(R.string.file_download_successfully))
                                            }
                                            return
                                        }
                                    }

                                    HandleStatusCode(
                                        context,
                                        rootView!!,
                                        bodyString,
                                        commonRes!!,
                                        requestCode,
                                        webServiceType,
                                        retrofitResponseListener
                                    )
                                }
                                STATUS_401 -> {
                                    if (SHOW_SESSION_EXPIRE_DIALOG) {
                                        context.showDialogWithOneButton(
                                            context.getString(R.string.session_expired),
                                            context.getString(R.string.please_log_in_again),
                                            context.getString(R.string.ok),
                                            object : AlertButtonClickListener {
                                                override fun onAlertClick(
                                                    dialog: DialogInterface,
                                                    which: Int
                                                ) {
                                                    dialog.dismiss()
                                                    apiBroadcast(API_BROADCAST_STATUS_CODE_401)
                                                }
                                            })

                                    } else {
                                        context.showToast(response.message())
                                        retrofitResponseListener.onSuccess(
                                            response.body()?.string(),
                                            requestCode
                                        )
                                        apiBroadcast(API_BROADCAST_STATUS_CODE_401)
                                    }
                                }
                                STATUS_426 -> {
                                    if (SHOW_APP_UPDATE_DIALOG) {
                                        val commonRes = Gson().fromJson(response.errorBody()?.string(), CommonRes::class.java)
                                        context.showDialogWithOneButton(
                                            context.getString(R.string.new_version_available),
                                            commonRes?.message,
                                            context.getString(R.string.update),
                                            object : AlertButtonClickListener {
                                                override fun onAlertClick(
                                                    dialog: DialogInterface,
                                                    which: Int
                                                ) {
                                                    IntentUtility(context).launchPlayStoreWithPackageName(
                                                        context.packageName
                                                    )
                                                    apiBroadcast(API_BROADCAST_STATUS_CODE_426)
                                                }
                                            }, false)

                                    } else {
                                        context.showToast(response.message())
                                        retrofitResponseListener.onSuccess(
                                            response.body()?.string(),
                                            requestCode
                                        )
                                        apiBroadcast(API_BROADCAST_STATUS_CODE_426)
                                    }
                                }
                                else -> {
                                    context.showToast(response.message())
                                    retrofitResponseListener.onSuccess(
                                        response.body()?.string(),
                                        requestCode
                                    )
                                }
                            }

                            if (LOADING_DIALOG_SHOW) {
                                frameLayout!!.removeView(progressView)
                            }

                        } catch (e: Exception) {
                            if (e.message != null && e.message!!.isNotEmpty()) {
                                context.showToast(e.message)
                            }
                            e.printStackTrace()
                            if (LOADING_DIALOG_SHOW) {
                                frameLayout!!.removeView(progressView)
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        if (LOADING_DIALOG_SHOW) {
                            frameLayout!!.removeView(progressView)
                        }

                        if (t is UnknownServiceException) {
                            if (t.message!!.contains("CLEARTEXT")) {
                                context.showToast(context.getString(R.string.api_level_pie_cleartext_support_disabled))
                            }
                        } else if (t is SocketTimeoutException) {
                            handleNoInternetTimoutDialog(context.getString(R.string.timeout))
                        }

                        context.showToast(t.message)

                        retrofitResponseListener.onFailure(t, requestCode)
                    }
                })

            } else {
                if (LOADING_DIALOG_SHOW) {
                    frameLayout!!.removeView(progressView)
                }
                context.showToast(context.getString(R.string.something_wrong))
            }
        } else {
            handleNoInternetTimoutDialog((context.getString(R.string.no_internet)))
        }
    }

    fun apiBroadcast(statusCode: String) {
        LocalBroadcastManager.getInstance(context)
            .sendBroadcast(Intent(API_BROADCAST).putExtra(API_BROADCAST_DATA, statusCode))
    }

    fun handleNoInternetTimoutDialog(type: String) {

//        if (type.equals(context.getString(R.string.timeout), true)) {
//
//        } else if (type.equals(context.getString(R.string.no_internet), true)) {
//
//        }

        val dialog = CustomDialog(context, R.style.full_screen_dialog).showDialog(
            R.layout.dialog_for_api,
            false,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        dialog.tvApiDialog.text = type

        dialog.ivApiDialogRetry!!.setOnClickListener {
            if (context.isOnline()) {
                if (dialog.isShowing) {
                    dialog.dismiss()
                }
                call()
            } else {
                context.showToast(context.getString(R.string.check_your_connection))
            }
        }
    }

    private fun getMapFromGson(json: String?): Map<String, Any> {
        if (json != null && !json.equals("null", true) && !json.equals("{}", true)) {
            return Gson().fromJson(json, object : TypeToken<HashMap<String, Any>>() {}.type)
        }
        return HashMap<String, String>()
    }


    interface RetrofitResponseListener {
        fun onSuccess(response: String?, apiRequestCode: Int)
        fun onFailure(t: Throwable, apiRequestCode: Int)
    }


    @JvmSuppressWildcards
    internal interface ApiInterface {

        @POST
        fun postRaw(@HeaderMap mapHeader: Map<String, Any>, @Url url: String, @Body requestBody: RequestBody): Call<ResponseBody>

        @Multipart
        @POST
        fun postFormData(@HeaderMap mapHeader: Map<String, Any>, @Url url: String, @Part filesList: List<MultipartBody.Part>, @PartMap() partMap: Map<String, RequestBody>): Call<ResponseBody>

        @GET
        fun get(@HeaderMap mapHeader: Map<String, Any>, @Url url: String, @QueryMap queryMap: Map<String, Any>): Call<ResponseBody>

    }

    class RequestType {

        companion object {
            const val GET = "get"
            const val POST = "post"
            const val POST_FORM_DATA = "post_form_data"
        }
    }

    class WebServiceType {

        @StringDef(
            WS_SIMPLE,
            WS_SIMPLE_WITH_MESSAGE,
            WS_FILE_DOWNLOAD,
            WS_FILE_DOWNLOAD_WITH_MESSAGE
        )
        annotation class Type

        companion object {
            const val WS_SIMPLE = "ws_simple"
            const val WS_SIMPLE_WITH_MESSAGE = "ws_simple_with_message"
            const val WS_SIMPLE_WITH_SUCCESS_MESSAGE = "ws_simple_with_success_message"
            const val WS_SIMPLE_WITH_SUCCESS = "ws_simple_with_success"
            const val WS_FILE_DOWNLOAD = "ws_file_download"
            const val WS_FILE_DOWNLOAD_WITH_MESSAGE = "ws_file_download_with_message"
        }
    }

    companion object {
        var BASE_URL = ""
        var HEADER_MAP: HashMap<String, Any>? = null
        var LOADING_TITLE = "Loading.."
        var DIALOG_FULLSCREEN = true
        private var retrofit: Retrofit? = null
        var LOADING_DIALOG_SHOW = true
        var INTERNET_DIALOG_SHOW = true
        var HANDLE_STATUS = true
        var MULTIPART_MODAL_LIST: ArrayList<MultipartModal>? = null
        var FILE_DOWNLOAD_PATH = Environment.getExternalStorageDirectory().path + "/"
        var SHOW_SESSION_EXPIRE_DIALOG = true
        var SHOW_APP_UPDATE_DIALOG = true
    }


}