package ru.otus.android.utils

import android.app.Notification
import android.app.Notification.Builder
import android.app.PendingIntent
import android.content.Context
import ru.otus.android.service.R.mipmap

class NotificationFactory(
    private val context: Context,
    private val channelId: String = "channelId",
    private val pendingIntent: PendingIntent? = null,
) {

    private var lastTitle = ""
    private var lastText = ""

    fun create(
        title: String? = null,
        text: String? = null,
    ): Notification {
        context.createNotificationChannel(channelId)
        return context.createNotification(
            title = title?.also { lastTitle = it } ?: lastTitle,
            text = text?.also { lastText = it } ?: lastTitle,
            channelId = channelId,
            pendingIntent = pendingIntent,
        )
    }

    private fun Context.createNotification(
        title: String,
        text: String,
        channelId: String,
        pendingIntent: PendingIntent?,
    ) = Builder(this, channelId)
        .setContentTitle(title)
        .setContentText(text)
        .setSmallIcon(mipmap.ic_launcher)
        .setContentIntent(pendingIntent)
        .build()
}