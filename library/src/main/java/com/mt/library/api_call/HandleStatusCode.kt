package com.mt.library.api_call

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.mt.library.R
import com.mt.library.modals.CommonRes
import com.mt.library.topsnackbar.MySnackbar
import com.mt.library.util.AppConfig
import com.google.gson.Gson

class HandleStatusCode(
        val context: Context,
        val rootView: View,
        val commonRes: CommonRes,
        val requestCode: Int,
        val webServiceType: String,
        val retrofitResponseListener: ApiCall.RetrofitResponseListener
) {

    init {

        removeNoDataIfFound()

        when (commonRes.statusCode) {
            AppConfig().STATUS_200, AppConfig().STATUS_201, AppConfig().STATUS_204 -> {
                if (webServiceType.equals(ApiCall.WebServiceType.WS_SIMPLE_WITH_MESSAGE, true)) {
                    showSnackbar()
                }
                retrofitResponseListener.onSuccess(Gson().toJson(commonRes), requestCode)
            }
            AppConfig().STATUS_208, AppConfig().STATUS_400, AppConfig().STATUS_401, AppConfig().STATUS_409, AppConfig().STATUS_422 -> {
                showSnackbar()
                checkStatusWithSuccess()
            }
            AppConfig().STATUS_404 -> {
                setNoDataFound()
                checkStatusWithSuccess()
            }
            else -> {
                retrofitResponseListener.onSuccess(Gson().toJson(commonRes), requestCode)
            }
        }
    }

    private fun checkStatusWithSuccess() {
        if (webServiceType.equals(ApiCall.WebServiceType.WS_SIMPLE_WITH_SUCCESS, true)) {
            retrofitResponseListener.onSuccess(Gson().toJson(commonRes), requestCode)
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
            val constraintLayout = rootView.findViewWithTag("no_data_found") as ConstraintLayout?
            if (constraintLayout != null)
                viewGroup.removeView(constraintLayout)
        }
    }

    private fun showSnackbar() {
        MySnackbar.create(
                context,
                commonRes.message,
                MySnackbar.GRAVITY_BOTTOM,
                MySnackbar.DURATION_LENGTH_LONG
        ).show()
    }
}