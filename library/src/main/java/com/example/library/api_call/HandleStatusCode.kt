package com.example.library.api_call

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.library.R
import com.example.library.modals.CommonRes
import com.example.library.util.*
import com.google.gson.Gson

class HandleStatusCode(
    val context: Context,
    val rootView: View,
    val bodyString: String,
    val commonRes: CommonRes,
    val requestCode: Int,
    val webServiceType: String,
    val retrofitResponseListener: ApiCall.RetrofitResponseListener
) {

    init {

        removeNoDataIfFound()

        when (commonRes.statusCode) {
            STATUS_200, STATUS_201, STATUS_204 -> {
                if (webServiceType.equals(ApiCall.WebServiceType.WS_SIMPLE_WITH_MESSAGE, true) || webServiceType.equals(ApiCall.WebServiceType.WS_SIMPLE_WITH_SUCCESS_MESSAGE, true)) {
                    showSnackbar()
                }
                retrofitResponseListener.onSuccess(bodyString, requestCode)
            }
            STATUS_208 -> {
                showSnackbar()
                checkStatusWithSuccess()
            }
            STATUS_400 -> {
                showSnackbar()
                checkStatusWithSuccess()
            }
            STATUS_401 -> {
                showSnackbar()
                checkStatusWithSuccess()
            }
            STATUS_404 -> {
                setNoDataFound()
                checkStatusWithSuccess()
            }
            STATUS_409 -> {
                showSnackbar()
                checkStatusWithSuccess()
            }
            STATUS_422 -> {
                if (webServiceType.equals(ApiCall.WebServiceType.WS_SIMPLE_WITH_MESSAGE, true) || webServiceType.equals(ApiCall.WebServiceType.WS_SIMPLE_WITH_SUCCESS_MESSAGE, true)) {
                    showSnackbar()
                }
                checkStatusWithSuccess()
            }
            else -> {
                retrofitResponseListener.onSuccess(bodyString, requestCode)
            }
        }
    }

    private fun checkStatusWithSuccess(){
        if (webServiceType.equals(ApiCall.WebServiceType.WS_SIMPLE_WITH_SUCCESS, true) || webServiceType.equals(ApiCall.WebServiceType.WS_SIMPLE_WITH_SUCCESS_MESSAGE, true)) {
            retrofitResponseListener.onSuccess(bodyString, requestCode)
        }
    }

    private fun setNoDataFound() {
        LayoutInflater.from(context)
            .inflate(
                R.layout.no_data_found,
                (rootView as ViewGroup).findViewWithTag("root_layout"),
                true
            )
    }

    private fun removeNoDataIfFound() {
        val viewGroup = rootView.findViewWithTag("root_layout") as ViewGroup?
        if (viewGroup != null) {
            val constraintLayout = rootView.findViewWithTag("ic_api_dialog") as ConstraintLayout?
            if (constraintLayout != null)
                viewGroup.removeView(constraintLayout)
        }
    }

    private fun showSnackbar() {
        context.showToast(commonRes.message)
    }
}