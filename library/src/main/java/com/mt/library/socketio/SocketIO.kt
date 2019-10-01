package com.mt.library.socketio

import android.content.Context
import android.content.Intent
import com.mt.library.BuildConfig
import com.mt.library.util.JSONUtility
import com.mt.library.util.MyLog
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import io.socket.engineio.client.transports.WebSocket
import org.json.JSONObject


class SocketIO(val context: Context) {

    companion object {
        var socket: Socket? = null
        var SOCKET_URL = ""
    }

    val SOCKET_BROADCAST_EVENT = "socket_broadcast_event"
    val SOCKET_BROADCAST_EVENT_NAME = "socket_broadcast_event_name"
    val SOCKET_BROADCAST_EVENT_DATA = "socket_broadcast_event_data"

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
            socketDataReceive(Socket.EVENT_CONNECT)
            socketDataReceive(Socket.EVENT_DISCONNECT)
            socketDataReceive(Socket.EVENT_ERROR)
            socket!!.connect()
        }
    }

    fun socketDataSend(eventName: String, data: Any) {
        val eventData = Gson().toJson(data)
        MyLog().printLog(TAG, "socketDataSend-> EventName: $eventName EventData: $eventData")
        val obj = JSONObject(eventData)
        socket!!.emit(eventName, obj)
    }

    fun socketDataReceive(eventName: String) {
        socket!!.on(eventName, object : Emitter.Listener {
            override fun call(vararg args: Any?) {
                if (args.isNotEmpty()) {
                    val eventData = if (args[0] is String) args[0] as String else ""
                    MyLog().printLog(
                        TAG,
                        "socketDataReceive-> EventName: $eventName EventData: $eventData"
                    )
                    if (JSONUtility().isJSONValid(eventData)) {
                        sendDataToBroadcast(eventName, eventData)
                    } else {
                        if (BuildConfig.DEBUG) {
                            MyLog().printLog(TAG, "Something Wrong in Socket Response..!")
                        }
                    }
                }
            }
        })
    }

    fun checkEventConnected(eventName: String): Boolean {
        val hasListener = socket!!.hasListeners(eventName)
        MyLog().printLog(TAG, "checkEventConnected: $hasListener")
        return hasListener
    }

    fun socketEventStop(eventName: String) {
        socket!!.off(eventName)
        MyLog().printLog(TAG, "socketEventStop: $eventName")
    }

    fun sendDataToBroadcast(eventName: String, eventData: String) {
        val intent = Intent(SOCKET_BROADCAST_EVENT)
        intent.putExtra(SOCKET_BROADCAST_EVENT_NAME, eventName)
        intent.putExtra(SOCKET_BROADCAST_EVENT_DATA, eventData)
        context.sendBroadcast(intent)
    }

    fun socketDisconnect() {
        socket!!.disconnect()
        MyLog().printLog(TAG, "socketDisconnect")
    }

    fun socketConnected(): Boolean {
        val isConnected = socket!!.connected()
        MyLog().printLog(TAG, "socketConnected: $isConnected")
        return isConnected
    }
}