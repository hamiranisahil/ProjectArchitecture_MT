package com.example.library.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import java.util.concurrent.atomic.AtomicInteger


class NotificationUtility(val context: Context) {

    companion object {
        var notificationManager: NotificationManager? = null
    }


    private fun getNotificationManager(): NotificationManager {
        if (notificationManager == null) {
            notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
        return notificationManager!!
    }


    fun simpleNotification(title: String?, message: String?, icon: Int) {
        createNotificationChannel("1", "Fetii Customer Notification", Priority.HIGH_PRIORITY)
        val builder = NotificationCompat.Builder(context, "1")
        builder.setContentTitle(title)
        builder.setContentText(message)
        builder.setSmallIcon(icon)
        builder.setAutoCancel(true)
        builder.priority = NotificationCompat.PRIORITY_HIGH
//        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//        builder.setContentIntent(pendingIntent)
        val notification = builder.build()
        getNotificationManager().notify(NotificationID.id, notification)
    }

    fun bigTextNotification(
        title: String,
        message: String,
        bigText: String,
        icon: Int,
        intent: Intent?
    ) {
        createNotificationChannel("1", "Fetii Customer Notification", Priority.HIGH_PRIORITY)
        val builder = NotificationCompat.Builder(context, "1")
        builder.setContentTitle(title)
        builder.setContentText(message)
        builder.setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
        builder.setSmallIcon(icon)
        builder.setAutoCancel(true)
        builder.priority = NotificationCompat.PRIORITY_HIGH
        if (intent != null) {
            val pendingIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            builder.setContentIntent(pendingIntent)
        }
        val notification = builder.build()
        getNotificationManager().notify(NotificationID.id, notification)

//        val builder = NotificationCompat.Builder(context, channelId)
//        builder.setContentTitle(title)
//        builder.setContentText(message)
//
//        builder.setSmallIcon(icon)
//        builder.setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
//        builder.setPriority(NotificationCompat.PRIORITY_HIGH)
//        getNotificationManager().notify(0, builder.build())
    }

    fun customBigNotification(layout: Int) {
        val remoteView = RemoteViews(context.packageName, layout)
//        val builder =
//            NotificationCompat.Builder(context, channelId).setSmallIcon(R.drawable.k_).setColor(Color.WHITE)
//                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
//                .setPriority(Notification.PRIORITY_MAX).setCustomBigContentView(remoteView)
//                .setDefaults(Notification.DEFAULT_LIGHTS)
//                .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.applicationContext.getPackageName() + "/" + R.raw.water_drop))
//        getNotificationManager().notify(0, builder.build())
    }

//    fun customNotification(
//        layout: Int,
//        remoteViewsListener: RemoteViewsListener,
//        channelId: String,
//        channelName: String,
//        smallIcon: Int,
//        sound: Uri?,
//        priority: Priority,
//        notificationId: Int
//    ) {
//
//        val CHANNEL_ID = "my_channel_01"
//        val name = "my_channel"
//        val Description = "This is my channel"
//
//        val NOTIFICATION_ID = 234
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            val importance = NotificationManager.IMPORTANCE_HIGH
//            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
//            mChannel.description = Description
//            mChannel.enableLights(true)
//            mChannel.lightColor = Color.RED
//            mChannel.enableVibration(true)
//            mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
//            mChannel.setShowBadge(true);
//
//            getNotificationManager().createNotificationChannel(mChannel)
//        }
//
//        val resultIntent = Intent(context, HomeActivity::class.java)
//        val stackBuilder = TaskStackBuilder.create(context)
//        stackBuilder.addParentStack(HomeActivity::class.java)
//        stackBuilder.addNextIntent(resultIntent)
//        val resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
//
//        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
//            .setContentTitle("TITLE")
//            .setContentText("SUB-TITLE")
//            .setStyle(NotificationCompat.BigTextStyle().bigText("Notice that the NotificationCompat.Builder constructor requires that you provide a channel ID. This is required for compatibility with Android 8.0 (API level 26) and higher, but is ignored by older versions By default, the notification's text content is truncated to fit one line. If you want your notification to be longer, you can enable an expandable notification by adding a style template with setStyle(). For example, the following code creates a larger text area"))
//            .setSmallIcon(R.mipmap.ic_launcher)
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            .setAutoCancel(true)
//            .addAction(R.drawable.ic_launcher_foreground, "Call", resultPendingIntent)
//
//        getNotificationManager().notify(NOTIFICATION_ID, builder.build())
//
//    }
//

    fun customNotification(
        layout: Int,
        remoteViewsListener: RemoteViewsListener,
        channelId: String,
        channelName: String,
        smallIcon: Int,
        sound: Uri?,
        priority: Priority,
        notificationId: Int
    ) {
        createNotificationChannel(channelId, channelName, priority)

        val remoteViews = RemoteViews(context.packageName, layout)
        remoteViewsListener.onCreateRemoteView(remoteViews)

        val builder =
            NotificationCompat.Builder(context, channelId).setPriority(Notification.PRIORITY_MAX)
                .setCustomContentView(remoteViews)

        setPriorityBelowOreo(builder, priority)

//        Set Small Icon : If smallIcon value is -1 then set default("ic_launcher_foreground") icon otherwise set it's value.
        if (smallIcon != -1) {
            builder.setSmallIcon(smallIcon)
        } else {
            builder.setSmallIcon(android.R.drawable.ic_notification_overlay)
        }

        if (sound != null) {
            builder.setSound(sound)
        }

        getNotificationManager().notify(notificationId, builder.build())
    }

    fun cancelAll() {
        getNotificationManager().cancelAll()
    }


    //    Requires Android Oreo for Notification
    fun createNotificationChannel(channelId: String, channelName: String, priority: Priority) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationCHannel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            when (priority) {
                Priority.LOW_PRIORITY -> {
                    notificationCHannel.importance = NotificationManager.IMPORTANCE_LOW
                }
                Priority.HIGH_PRIORITY -> {
                    notificationCHannel.importance = NotificationManager.IMPORTANCE_HIGH
                }
                else -> {
                    notificationCHannel.importance = NotificationManager.IMPORTANCE_DEFAULT
                }
            }
            notificationCHannel.description = "Description"
            notificationCHannel.enableLights(true)
            notificationCHannel.lightColor = Color.RED
            notificationCHannel.enableVibration(true)
            notificationCHannel.vibrationPattern =
                longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            getNotificationManager().createNotificationChannel(notificationCHannel)

        }
    }

    //    Set Priority below oreo devices
    @Suppress("DEPRECATION")
    fun setPriorityBelowOreo(builder: NotificationCompat.Builder, priority: Priority) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            when (priority) {
                Priority.LOW_PRIORITY -> {
                    builder.priority = Notification.PRIORITY_LOW
                }
                Priority.HIGH_PRIORITY -> {
                    builder.priority = Notification.PRIORITY_MAX
                }
                else -> {
                    builder.priority = Notification.PRIORITY_DEFAULT
                }
            }
        }
    }

    object NotificationID {
        private val c = AtomicInteger(0)
        val id: Int
            get() = c.incrementAndGet()
    }

    enum class Priority {
        DEFAULT_PRIORITY, LOW_PRIORITY, HIGH_PRIORITY
    }

    interface RemoteViewsListener {
        fun onCreateRemoteView(remoteViews: RemoteViews)
    }
}