package com.example.library.api_call

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import com.example.library.app_permissions.PermissionManagerUtility
import com.example.library.util.REQUEST_CODE_READ_EXTERNAL_STORAGE
import okhttp3.ResponseBody
import java.io.*

class ApiFileDownloader(
    val context: Context,
    val responseBody: ResponseBody,
    val url: String,
    requestCode: Int,
    val retrofitResponseListener: ApiCall.RetrofitResponseListener
) {

    init {
        PermissionManagerUtility().requestPermission(
            context,
            false,
            REQUEST_CODE_READ_EXTERNAL_STORAGE,
            object : PermissionManagerUtility.PermissionListener {
                override fun onAppPermissions(
                    grantPermissions: ArrayList<String>,
                    deniedPermissions: ArrayList<String>
                ) {
                    try {
                        val directory = File(ApiCall.FILE_DOWNLOAD_PATH)
                        if (!directory.exists()) {
                            directory.mkdirs()
                        }
                        val downloadFile = File(directory, url.substring(url.lastIndexOf('/') + 1))

                        var inputStream: InputStream? = null
                        var outputStream: OutputStream? = null

                        try {
                            val fileReader = ByteArray(4096)
//                                val fileSize = responseBody.contentLength()
                            var fileSizeDownloaded: Long = 0

                            inputStream = responseBody.byteStream()
                            outputStream = FileOutputStream(downloadFile)

                            while (true) {
                                val read = inputStream!!.read(fileReader)
                                if (read == -1) {
                                    break
                                }

                                outputStream.write(fileReader, 0, read)
                                fileSizeDownloaded += read.toLong()

                            }
                            outputStream.flush()

                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        } finally {
                            inputStream?.close()
                            outputStream?.close()
                        }
                        retrofitResponseListener.onSuccess(
                            downloadFile.path.toString(),
                            requestCode
                        )
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

            },
            READ_EXTERNAL_STORAGE,
            WRITE_EXTERNAL_STORAGE
        )
    }

}