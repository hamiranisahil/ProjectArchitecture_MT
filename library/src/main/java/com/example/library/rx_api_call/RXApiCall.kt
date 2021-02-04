package com.example.library.rx_api_call

import android.app.Activity
import android.content.Context
import android.os.Environment
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringDef
import com.example.library.R
import com.example.library.dialog.CustomDialog
import com.example.library.modals.CommonRes
import com.example.library.modals.MultipartModal
import com.example.library.util.isOnline
import com.example.library.util.showToast
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.dialog_for_api.*
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.File
import java.io.StringReader
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

class RXApiCall {

    lateinit var context: Context
    private lateinit var requestParams: Array<Any>
    lateinit var paramsBody: Any
    lateinit var webServiceType: String

    internal var url = ""
    private var TAG = "ApiCall"
    var method = ""
    var requestCode = -1
    private var rootView: View? = null
    private var apiInterface: ApiInterface
    var jsonString: String? = null
    var requestTag: Int = 0

    constructor(
        context: Context,
        requestParams: Array<Any>,
        paramsBody: Any,
        @WebServiceType.Type webServiceType: String,
        requestTag: Int = 0
    ) {
        this.context = context
        this.requestParams = requestParams
        this.paramsBody = paramsBody
        this.webServiceType = webServiceType
        this.requestTag = requestTag
        url = requestParams[0] as String
        method = requestParams[1] as String
        requestCode = requestParams[2] as Int
        LOADING_DIALOG_SHOW = if (requestParams.size > 3) requestParams[3] as Boolean else true
        KEYBOARD_HIDE = if (requestParams.size > 4) requestParams[4] as Boolean else true

        rootView =
            if (context is Activity) context.window.decorView.rootView.findViewById(android.R.id.content) else null

        handleNormalCall()
    }

    fun setMultiPartList(multipartModalList: ArrayList<MultipartModal>?,){
        MULTIPART_MODAL_LIST = multipartModalList
    }

    private val client: Retrofit
        get() {
            if (retrofit == null) {
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                if (API_LOG_ENABLE) {
                    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                }
                val okhttpBuilder = OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor)
                okhttpBuilder.connectTimeout(2, TimeUnit.MINUTES)
                okhttpBuilder.readTimeout(2, TimeUnit.MINUTES)
                okhttpBuilder.writeTimeout(2, TimeUnit.MINUTES)
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okhttpBuilder.build())
                    .build()
            }
            return retrofit!!
        }

    init {
        apiInterface = client.create(ApiInterface::class.java)
        if (HEADER_MAP == null) {
            HEADER_MAP = HashMap()
        }
    }

    private fun <T> getClassType(typeOfT: Type) : T {
        val typeToken: TypeToken<T> = TypeToken.get(typeOfT) as TypeToken<T>
        val typeAdapter: TypeAdapter<T> = Gson().getAdapter(typeToken)
        return typeAdapter.read(null)
    }

    fun call(): Observable<CommonRes>? {
        if (INTERNET_DIALOG_SHOW && context.isOnline()) {
            jsonString = Gson().toJson(paramsBody)

            when (method) {
                RequestType.GET -> {
                    if (paramsBody is String) {
                        return apiInterface.get(
                            HEADER_MAP!!,
                            url + paramsBody,
                            getMapFromGson(null)
                        )
                    } else {
                        return apiInterface.get(HEADER_MAP!!, url, getMapFromGson(jsonString))
                    }
                }
                RequestType.POST -> {
                    return apiInterface.postRaw(
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
                    return apiInterface.postFormData(HEADER_MAP!!, url, filesList, partMap)
                }
                else -> {
                    return null
                }
            }


        } else {
            handleNoInternetTimoutDialog((context.getString(R.string.no_internet)))
            return null
        }
    }

    fun callWithResponse(): Observable<ResponseBody>? {
        if (INTERNET_DIALOG_SHOW && context.isOnline()) {
            jsonString = Gson().toJson(paramsBody)

            when (method) {
                RequestType.GET -> {
                    if (paramsBody is String) {
                        return apiInterface.getResponse(
                            HEADER_MAP!!,
                            url + paramsBody,
                            getMapFromGson(null)
                        )
                    } else {
                        return apiInterface.getResponse(HEADER_MAP!!, url, getMapFromGson(jsonString))
                    }
                }
                RequestType.POST -> {
                    return apiInterface.postRawResponse(
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
                    return apiInterface.postFormDataResponse(HEADER_MAP!!, url, filesList, partMap)
                }
                else -> {
                    return null
                }
            }


        } else {
            handleNoInternetTimoutDialog((context.getString(R.string.no_internet)))
            return null
        }
    }

    fun storeApiObserver(apiRequestCode: Int, disposable: Disposable){
        MAINTAIN_API_CALLS[apiRequestCode] = disposable
    }

    private fun handleNormalCall() {

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
            } else {
                context.showToast(context.getString(R.string.check_your_connection))
            }
        }
    }

    private fun getMapFromGson(json: String?): Map<String, Any> {
        if (json != null && !json.equals("null", true) && !json.equals("{}", true)) {
            /*val gson = GsonBuilder().registerTypeAdapter(Double::class.java, object :
                JsonSerializer<Double> {
                override fun serialize(
                    src: Double?,
                    typeOfSrc: Type?,
                    context: JsonSerializationContext?
                ): JsonElement {
                    if(src is Double)
                        return JsonPrimitive(src);
                    return JsonPrimitive(src);
                }

            }).create()
            return gson.fromJson(json, object : TypeToken<HashMap<String, Any>>() {}.type)*/
            return Gson().fromJson(json, object : TypeToken<HashMap<String, Any>>() {}.type)
        }
        return HashMap<String, String>()
    }

    @JvmSuppressWildcards
    internal interface ApiInterface {

        @POST
        fun  postRaw(
            @HeaderMap mapHeader: Map<String, Any>,
            @Url url: String,
            @Body requestBody: RequestBody
        ): Observable<CommonRes>

        @Multipart
        @POST
        fun postFormData(
            @HeaderMap mapHeader: Map<String, Any>,
            @Url url: String,
            @Part filesList: List<MultipartBody.Part>,
            @PartMap() partMap: Map<String, RequestBody>
        ): Observable<CommonRes>

        @GET
        fun get(
            @HeaderMap mapHeader: Map<String, Any>,
            @Url url: String,
            @QueryMap queryMap: Map<String, Any>
        ): Observable<CommonRes>


        @POST
        fun  postRawResponse(
            @HeaderMap mapHeader: Map<String, Any>,
            @Url url: String,
            @Body requestBody: RequestBody
        ): Observable<ResponseBody>

        @Multipart
        @POST
        fun postFormDataResponse(
            @HeaderMap mapHeader: Map<String, Any>,
            @Url url: String,
            @Part filesList: List<MultipartBody.Part>,
            @PartMap() partMap: Map<String, RequestBody>
        ): Observable<ResponseBody>

        @GET
        fun getResponse(
            @HeaderMap mapHeader: Map<String, Any>,
            @Url url: String,
            @QueryMap queryMap: Map<String, Any>
        ): Observable<ResponseBody>
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
            const val WS_WITH_SUCCESS_MESSAGE = "ws_with_success_message"
            const val WS_WITH_SUCCESS = "ws_with_success"
            const val WS_ONLY_SUCCESS = "ws_only_success"
            const val WS_ONLY_SUCCESS_MESSAGE = "ws_only_success_message"
            const val WS_FILE_DOWNLOAD = "ws_file_download"
            const val WS_FILE_DOWNLOAD_WITH_MESSAGE = "ws_file_download_with_message"
        }
    }

    interface RXRetrofitResponseListener {
        fun <T> onCreateObservable(observable: Observable<T>)
    }

    companion object {
        var BASE_URL = ""
        var API_LOG_ENABLE = true
        var HEADER_MAP: HashMap<String, Any>? = null
        var LOADING_TITLE = "Loading..."
        var DIALOG_FULLSCREEN = true
        var retrofit: Retrofit? = null
        var LOADING_DIALOG_SHOW = true
        var INTERNET_DIALOG_SHOW = true
        var KEYBOARD_HIDE = true
        var HANDLE_STATUS = true
        var MULTIPART_MODAL_LIST: ArrayList<MultipartModal>? = null
        private var MAINTAIN_API_CALLS = HashMap<Int, Disposable>()
        var FILE_DOWNLOAD_PATH = Environment.getExternalStorageDirectory().path + "/"
        var SHOW_SESSION_EXPIRE_DIALOG = true
        var SHOW_APP_UPDATE_DIALOG = true
        var API_CALL_LOADING_SCREEN_TYPE: ApiCallLoadingScreenType =
            ApiCallLoadingScreenType.FULL_SCREEN_DIALOG

        fun removeApiObserver(apiRequestCode: Int){
            MAINTAIN_API_CALLS[apiRequestCode]?.dispose()
            MAINTAIN_API_CALLS.remove(apiRequestCode)
        }
    }

    enum class ApiCallLoadingScreenType {
        INNER_SCREEN, FULL_SCREEN_DIALOG
    }

}