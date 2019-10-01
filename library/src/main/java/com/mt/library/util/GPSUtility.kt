package com.mt.library.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.location.LocationManager
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener

class GPSUtility(val context: Context, val onGpsListener: OnGpsListener) {

    private var settingsClient: SettingsClient = LocationServices.getSettingsClient(context)
    private var locationSettingsRequest: LocationSettingsRequest
    private var locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private var locationRequest: LocationRequest = LocationRequest.create()
    val TAG = GPSUtility::class.java.simpleName
    val GPS_REQUEST_CODE = 1

    init {
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = (10 * 1000)
        locationRequest.fastestInterval = (2 * 1000)
        val locationSettingsBuilder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        locationSettingsRequest = locationSettingsBuilder.build()
        locationSettingsBuilder.setAlwaysShow(true)
    }

    fun turnGpsOn() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            onGpsListener.onGpsStatusChange(true)

        } else {
            settingsClient.checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(context as Activity, object : OnSuccessListener<LocationSettingsResponse> {
                    override fun onSuccess(p0: LocationSettingsResponse?) {
                        onGpsListener.onGpsStatusChange(true)
                    }
                }).addOnFailureListener(context, object : OnFailureListener {
                override fun onFailure(e: Exception) {
                    when ((e as ApiException).statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                            try {
                                val rae = e as ResolvableApiException
                                rae.startResolutionForResult(context, GPS_REQUEST_CODE)

                            } catch (sie: IntentSender.SendIntentException) {
                                MyLog().printLog(TAG, "PendingIntent unable to execute request")
                            }
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            val errorMessage =
                                "Location settings are inadequate, and cannot be fixed here. Fix in Settings."
                            MyLog().printLog(TAG, errorMessage)
                        }
                    }
                }

            })


        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GPS_REQUEST_CODE) {
                onGpsListener.onGpsStatusChange(true)
            } else {
                onGpsListener.onGpsStatusChange(false)
            }
        } else {
            onGpsListener.onGpsStatusChange(false)
        }

    }

    interface OnGpsListener {
        fun onGpsStatusChange(isGpsEnabled: Boolean)
    }

}