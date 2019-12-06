package com.example.library.socketio

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.library.BuildConfig
import com.example.library.R
import com.example.library.modals.CommonRes
import com.example.library.util.*
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.WebSocket
import org.jetbrains.anko.runOnUiThread
import org.json.JSONObject


class SocketIO(val context: Context) {

    companion object {
        var socket: Socket? = null
        var SOCKET_URL = ""
        var SHOW_SESSION_EXPIRE_DIALOG = true
    }

    val TAG = SocketIO::class.java.simpleName

    init {
        if (socket == null) {
            val options = IO.Options()
            options.transports = arrayOf(WebSocket.NAME)
            options.reconnection = true
            options.forceNew = false
            options.reconnectionDelay = 1000
            options.timeout = 10000
            socket = IO.socket(SOCKET_URL)
            socketConnect()
        }
    }

    fun socketDataSend(eventName: String, data: Any) {
        val eventData = Gson().toJson(data)
        printLog(TAG, "socketDataSend-> EventName: $eventName EventData: $eventData")
        val obj = JSONObject(eventData)
        socket!!.emit(eventName, obj)
    }

    fun socketDataReceive(eventName: String) {
        if (checkEventConnected(eventName).not()) {
            socket!!.on(eventName) { args ->
                if (args.isNotEmpty()) {
                    val eventData = if (args[0] is String) args[0] as String else ""
                    printLog(
                        TAG,
                        "socketDataReceive-> EventName: $eventName EventData: $eventData"
                    )
                    if (isValidJSON(eventData)) {
                        if (SHOW_SESSION_EXPIRE_DIALOG) {
                            val commonRes = Gson().fromJson(eventData, CommonRes::class.java)
                            if (commonRes.statusCode == 401) {
                                handleUnauthorizeDialog()

                            } else {
                                sendDataToBroadcast(eventName, eventData)
                            }
                        } else {
                            sendDataToBroadcast(eventName, eventData)
                        }
                    } else {
                        if (BuildConfig.DEBUG) {
                            printLog(TAG, "Something Wrong in Socket Response..!")
                        }
                    }
                }
            }
        }
    }

    private fun handleUnauthorizeDialog() {
        context.runOnUiThread {
            context.showDialogWithOneButton(context.getString(R.string.session_expired),
                context.getString(
                    R.string.please_log_in_again
                ),
                context.getString(R.string.ok),
                object :
                    AlertButtonClickListener {
                    override fun onAlertClick(
                        dialog: DialogInterface,
                        which: Int
                    ) {
                        socketBroadcast()
                    }
                })
        }
    }

    private fun socketBroadcast() {
        socket?.disconnect()
        LocalBroadcastManager.getInstance(context).sendBroadcast(
            Intent(SOCKET_BROADCAST).putExtra(
                SOCKET_BROADCAST_EVENT_DATA, SOCKET_BROADCAST_401
            )
        )
    }

    fun checkEventConnected(eventName: String): Boolean {
        val hasListener = socket!!.hasListeners(eventName)
        printLog(TAG, "checkEventConnected: $hasListener")
        return hasListener
    }

    fun socketEventStop(eventName: String) {
        socket!!.off(eventName)
        printLog(TAG, "socketEventStop: $eventName")
    }

    fun sendDataToBroadcast(eventName: String, eventData: String) {
        val intent = Intent(SOCKET_BROADCAST)
        intent.putExtra(SOCKET_BROADCAST_EVENT_NAME, eventName)
        intent.putExtra(SOCKET_BROADCAST_EVENT_DATA, eventData)
        context.sendBroadcast(intent)
    }

    fun socketDisconnect() {
        socket!!.disconnect()
        printLog(TAG, "socketDisconnect")
    }

    fun socketConnect() {
        if (socketConnected().not()) {
            socketDataReceive(Socket.EVENT_CONNECT)
            socketDataReceive(Socket.EVENT_DISCONNECT)
            socketDataReceive(Socket.EVENT_ERROR)
            socket!!.connect()
        }
    }

    fun socketConnected(): Boolean {
        val isConnected = socket!!.connected()
        printLog(TAG, "socketConnected: $isConnected")
        return isConnected
    }
}