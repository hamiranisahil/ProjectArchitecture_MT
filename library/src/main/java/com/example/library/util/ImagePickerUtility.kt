package com.example.library.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AlertDialog
import com.example.library.app_permissions.PermissionManagerUtility
import java.io.*


class ImagePickerUtility(val context: Context) {

    companion object {
        lateinit var sSelectImageListener: SelectImageListener
    }


    private fun selectImage() {
        val items = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Add Photo!")
        builder.setItems(items) { dialog, item ->
            when {
                items[item] == "Take Photo" -> cameraIntent()
                items[item] == "Choose from Gallery" -> galleryIntent()
                items[item] == "Cancel" -> dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun cameraIntent() {
        PermissionManagerUtility().requestPermission(
            context,
            false,
            REQUEST_CODE_CAMERA,
            object : PermissionManagerUtility.PermissionListener {
                override fun onAppPermissions(
                    grantPermissions: ArrayList<String>,
                    deniedPermissions: ArrayList<String>
                ) {
                    if (deniedPermissions.size <= 0) {
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        (context as Activity).startActivityForResult(intent, REQUEST_CAMERA)
                    }
                }
            },
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    private fun galleryIntent() {
        PermissionManagerUtility().requestPermission(
            context,
            false,
            REQUEST_CODE_CAMERA,
            object : PermissionManagerUtility.PermissionListener {
                override fun onAppPermissions(
                    grantPermissions: ArrayList<String>,
                    deniedPermissions: ArrayList<String>
                ) {
                    if (deniedPermissions.size <= 0) {
                        val intent = Intent()
                        intent.type = "image/*"
                        intent.action = Intent.ACTION_GET_CONTENT
                        (context as Activity).startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            REQUEST_GALLERY
                        )
                    }
                }
            },
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    @RequiresPermission(
        allOf = [Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE]
    )
    fun show(selectImageListener: SelectImageListener) {
        try {
            sSelectImageListener = selectImageListener
            selectImage()
        } catch (e: SecurityException) {
            printLog("PermissionManagerUtilit", "Security Exception: No Permissions Found.")
        }
    }

    @RequiresPermission(
        allOf = [Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE]
    )
    fun showCamera(selectImageListener: SelectImageListener) {
        try {
            sSelectImageListener = selectImageListener
            cameraIntent()
        } catch (e: SecurityException) {
            printLog("PermissionManagerUtilit", "Security Exception: No Permissions Found.")
        }
    }

    @RequiresPermission(
        allOf = [Manifest.permission.READ_EXTERNAL_STORAGE]
    )
    fun showGallery(selectImageListener: SelectImageListener) {
        try {
            sSelectImageListener = selectImageListener
            galleryIntent()
        } catch (e: SecurityException) {
            printLog("PermissionManagerUtilit", "Security Exception: No Permissions Found.")
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_GALLERY -> onSelectFromGalleryResult(data)
                REQUEST_CAMERA -> onCaptureImageResult(data)
            }
        }
    }

    private fun onSelectFromGalleryResult(data: Intent?) {
        if (data != null) {
            try {
                val selectedImage = data.data!!
//                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
//                val cursor = sContext.getContentResolver().query(selectedImage, filePathColumn, null, null, null)
//                cursor!!.moveToFirst()
//                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
//                val picturePath = cursor.getString(columnIndex)
//                cursor.close()
                sSelectImageListener.onImageSelect(selectedImage)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun onCaptureImageResult(data: Intent?) {
        val thumbnail = data!!.extras!!.get("data") as Bitmap
        val bytes = ByteArrayOutputStream()
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val destination =
            File(context.cacheDir.path + "/", System.currentTimeMillis().toString() + ".jpg")
        val fo: FileOutputStream
        try {
            destination.createNewFile()
            fo = FileOutputStream(destination)
            fo.write(bytes.toByteArray())
            fo.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        sSelectImageListener.onImageSelect(Uri.fromFile(destination))
    }

    interface SelectImageListener {
        fun onImageSelect(uri: Uri)
    }
}