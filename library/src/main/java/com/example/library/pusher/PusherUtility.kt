package com.example.library.pusher

import android.content.Context
import com.example.library.util.printLog
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.channel.Channel
import org.jetbrains.anko.runOnUiThread

class PusherUtility(val context: Context) {

    companion object {
        var CLUSTER = ""
        var APP_KEY = ""
        private var pusher: Pusher? = null
        private var channel: Channel? = null
    }

    private var TAG = PusherUtility::class.java.simpleName

    init {
        if (pusher == null && CLUSTER != "" && APP_KEY != "") {
            val pusherOptions = PusherOptions()
            pusherOptions.setCluster(CLUSTER)
            pusher = Pusher(APP_KEY, pusherOptions)
        }
    }

    fun subscribeChannel(channelName: String): Boolean {
        printLog(TAG, "subscribeChannel: channelName: $channelName")
        channel = pusher?.getChannel(channelName)
        if (channel == null) {
            channel = pusher?.subscribe(channelName)
            return true
        }
        return false
    }

    fun hasChannelSubscribed(channelName: String): Boolean {
        channel = pusher?.getChannel(channelName)
        return channel?.isSubscribed ?: false
    }

    fun unsubscribeChannel(channelName: String) {
        printLog(TAG, "unsubscribeChannel: channelName: $channelName")
        val channel = pusher?.getChannel(channelName)
        if (channel != null && channel.isSubscribed) {
            pusher?.unsubscribe(channelName)
        }
    }

    fun subscribeEvent(
        eventName: String,
        subscribePusherListener: SubscribePusherListener
    ) {
        printLog(TAG, "onEvent: eventName: $eventName")
        channel?.bind(eventName) { channelName, event, data ->
            try {
                printLog(
                    TAG,
                    "onEvent: cNM: $channelName eNM: $event, data: $data"
                )
                context.runOnUiThread {
                    subscribePusherListener.onPusherEvent(channelName, event, data)
                }
            } catch (e: Exception) {
                printLog(TAG, "Exception: ${e.message}")
            }
        }
    }

    fun connect() {
        pusher?.connect()
        printLog(TAG, "connect")
    }

    interface SubscribePusherListener {
        fun onPusherEvent(
            channelName: String?,
            eventName: String,
            data: String?
        )
    }
}