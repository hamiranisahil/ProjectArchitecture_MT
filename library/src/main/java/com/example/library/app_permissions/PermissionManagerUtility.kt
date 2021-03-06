package com.example.library.app_permissions

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import com.example.library.util.AlertTwoButtonClickListener
import com.example.library.util.showDialogWithTwoButton

class PermissionManagerUtility {

    companion object {
        var isShown = false
        var mPermissionRequestCode = -1
        var mContext: Context? = null
        val grantPermissions = ArrayList<String>()
        val deniedPermissions = ArrayList<String>()
        var isCompulsory = false
        var mPermissions = ArrayList<String>()
        var mPermissionListener: PermissionListener? = null
    }

    constructor(){}

    constructor(context: Context){
        mContext = context
    }

    fun requestPermission(
        context: Context,
        isCompulsory: Boolean,
        permissionRequestCode: Int,
        permissionListener: PermissionListener,
        vararg permissions: String
    ) {
        isShown = false
        deniedPermissions.clear()
        mPermissions.clear()
        grantPermissions.clear()

        PermissionManagerUtility.isCompulsory = isCompulsory
        mContext = context
        mPermissionRequestCode = permissionRequestCode
        mPermissionListener = permissionListener
        permissions.toCollection(mPermissions)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in permissions) {
                if (!checkIsPermissionGranted(context, permission)) {
                    deniedPermissions.add(permission)
                    ActivityCompat.requestPermissions(
                        context as Activity,
                        permissions,
                        permissionRequestCode
                    )
                    break
                } else {
                    grantPermissions.add(permission)
                }
            }
            if (deniedPermissions.size <= 0 && grantPermissions.size > 0) {
                checkReject()
            }

        } else {
            callBack()
        }
    }

    fun checkIsPermissionGranted(context: Context, permission: String): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    //    Pass onRequestPermissionsResult from Activity
    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == mPermissionRequestCode) {
            grantPermissions.clear()
            deniedPermissions.clear()
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    grantPermissions.add(permissions[i])
                } else {
                    deniedPermissions.add(permissions[i])
                }
            }
            checkReject()
        }
    }

    //    Pass onActivityResult from Activity
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (mPermissionRequestCode == requestCode) {
            grantPermissions.clear()
            deniedPermissions.clear()
            for (permission in mPermissions) {
                if (ActivityCompat.checkSelfPermission(
                        mContext!!,
                        permission
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    grantPermissions.add(permission)
                } else {
                    deniedPermissions.add(permission)
                }
            }
            checkReject()
        }
    }

    fun openSettings(activity: Activity, mPermissionRequestCode: Int) {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.data = Uri.parse("package:" + PermissionManagerUtility.mContext?.packageName)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        activity.startActivityForResult(intent, mPermissionRequestCode)
    }

    fun checkReject() {
        if (deniedPermissions.size > 0) {
            if (isCompulsory) {
                showDialogForPermission()
            } else {

                if (isShown.not()) {
                    showDialogForPermission()
                    isShown = true
                }

//                callBack()
            }
        } else {
            callBack()
        }
    }

    private fun showDialogForPermission() {
        mContext?.showDialogWithTwoButton(
            "Need Permissions",
            "This App Needs Permissions",
            "Grant",
            "Cancel",
            object : AlertTwoButtonClickListener {
                override fun onAlertClick(
                    dialog: DialogInterface,
                    which: Int,
                    isPositive: Boolean
                ) {
                    dialog.dismiss()
                    if (isPositive) {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", mContext!!.packageName, null)
                        intent.data = uri
                        (mContext as Activity).startActivityForResult(
                            intent,
                            mPermissionRequestCode
                        )

                    } else {
                        if (isCompulsory) {
                            (mContext as Activity).finish()
                        }
                    }
                }
            })
    }

    private fun callBack() {
        mPermissionRequestCode = -1
        mPermissionListener!!.onAppPermissions(
            grantPermissions,
            deniedPermissions
        )
    }

    interface PermissionListener {
        fun onAppPermissions(
            grantPermissions: ArrayList<String>,
            deniedPermissions: ArrayList<String>
        )
    }
}