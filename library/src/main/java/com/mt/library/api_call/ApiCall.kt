package com.mt.library.api_call

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.StringDef
import com.mt.library.R
import com.mt.library.modals.CommonRes
import com.mt.library.modals.MultipartModal
import com.mt.library.topsnackbar.MySnackbar
import com.mt.library.util.AppConfig
import com.mt.library.util.MyLog
import com.mt.library.util.NetworkUtility
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
        multipartModalList: ArrayList<MultipartModal>,
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

                for (multipartModal in MULTIPART_MODAL_LIST!!) {
                    val file = File(context.cacheDir, File(multipartModal.filePath).name)
                    val part = MultipartBody.Part.createFormData(
                        multipartModal.fileKey,
                        file.name,
                        RequestBody.create(MediaType.parse("*/*"), file)
                    )
                    filesList.add(part)
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
                okhttpBuilder.connectTimeout(60, TimeUnit.SECONDS)
                okhttpBuilder.readTimeout(60, TimeUnit.SECONDS)
                okhttpBuilder.writeTimeout(60, TimeUnit.SECONDS)
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okhttpBuilder.build())
                    .build()
            }
            return retrofit!!
        }


    private fun call() {
        if (INTERNET_DIALOG_SHOW && NetworkUtility(context).isOnline()) {
            var progressView: View? = null
            var frameLayout: FrameLayout? = null
            if (LOADING_DIALOG_SHOW) {
                progressView = LayoutInflater.from(context).inflate(R.layout.loading_api_progress, null)
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
                            when (response.code()) {
                                AppConfig().STATUS_200 -> {
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

                                    MyLog().printLog(
                                        "ApiCall - Request",
                                        "Url: $url Method: $method RequestCode: $requestCode WebServiceType: $webServiceType FileDownloadPath: $FILE_DOWNLOAD_PATH"
                                    )
                                    MyLog().printLog("ApiCall - Request", "ParamsBody: $jsonString")
                                    MyLog().printLog(
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
                                                MySnackbar.create(
                                                    context,
                                                    "File Download Successfully",
                                                    MySnackbar.GRAVITY_TOP,
                                                    MySnackbar.DURATION_LENGTH_LONG
                                                ).show()
                                            }
                                            return
                                        }
                                    }

                                    HandleStatusCode(
                                        context,
                                        rootView!!,
                                        commonRes,
                                        requestCode,
                                        webServiceType,
                                        retrofitResponseListener
                                    )
                                }
                                AppConfig().STATUS_405 -> {
                                    MySnackbar.create(
                                        context,
                                        "Method not found",
                                        MySnackbar.GRAVITY_BOTTOM,
                                        MySnackbar.DURATION_LENGTH_LONG
                                    ).show()
                                }
                                AppConfig().STATUS_400 -> {
                                    MySnackbar.create(
                                        context,
                                        "Bad Request.",
                                        MySnackbar.GRAVITY_BOTTOM,
                                        MySnackbar.DURATION_LENGTH_LONG
                                    ).show()
                                }
                                AppConfig().STATUS_500 -> {
                                    MySnackbar.create(
                                        context,
                                        "Internal Server Error.",
                                        MySnackbar.GRAVITY_BOTTOM,
                                        MySnackbar.DURATION_LENGTH_LONG
                                    ).show()
                                }
                                AppConfig().STATUS_401 -> {
                                    MySnackbar.create(
                                        context,
                                        "Unauthorised Error.",
                                        MySnackbar.GRAVITY_BOTTOM,
                                        MySnackbar.DURATION_LENGTH_LONG
                                    ).show()
                                }
                            }

                            if (LOADING_DIALOG_SHOW) {
                                frameLayout!!.removeView(progressView)
                            }

                        } catch (e: Exception) {
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

                        if (t is SocketTimeoutException) {
                            handleNoInternetTimoutDialog(context.getString(R.string.timeout))
                        }
                        if (t is UnknownServiceException) {
                            if (t.message!!.contains("CLEARTEXT")) {
                                MySnackbar.create(
                                    context,
                                    "API Level 28 > CLEARTEXT Support Disabled..",
                                    MySnackbar.GRAVITY_BOTTOM,
                                    MySnackbar.DURATION_LENGTH_INDEFINITE
                                ).show()
                            }
                        }

                        retrofitResponseListener.onFailure(t, requestCode)
                    }
                })

            } else {
                if (LOADING_DIALOG_SHOW) {
                    frameLayout!!.removeView(progressView)
                }
                MySnackbar.create(
                    context,
                    "Something Wrong.",
                    MySnackbar.GRAVITY_BOTTOM,
                    MySnackbar.DURATION_LENGTH_LONG
                ).show()
            }
        } else {
            handleNoInternetTimoutDialog((context.getString(R.string.no_internet)))
        }
    }

    fun handleNoInternetTimoutDialog(type: String) {
        var view: View? = null
        var dialog: Dialog? = null
        var ivRetry: ImageView? = null

        if (type.equals(context.getString(R.string.timeout), true)) {
            view = LayoutInflater.from(context).inflate(R.layout.time_out, null)
            ivRetry = view.findViewById(R.id.iv_retry) as ImageView

        } else if (type.equals(context.getString(R.string.no_internet), true)) {
            view = LayoutInflater.from(context).inflate(R.layout.no_internet, null)
            ivRetry = view.findViewById(R.id.iv_retry) as ImageView
        }

        if (DIALOG_FULLSCREEN) {
            dialog = Dialog(context, R.style.full_screen_dialog)
            dialog.setContentView(view!!)
            dialog.setCancelable(false)
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            dialog.show()
        }

        ivRetry!!.setOnClickListener {
            if (NetworkUtility(context).isOnline()) {
                if (dialog != null && dialog.isShowing) {
                    dialog.dismiss()
                }
                call()
            } else {
                MySnackbar.create(
                    context,
                    "Check Your Connection..!",
                    MySnackbar.GRAVITY_BOTTOM,
                    MySnackbar.DURATION_LENGTH_LONG
                )
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
    }

}