package ru.otus.android.finish.services

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import ru.otus.android.domain.FileUploader
import ru.otus.android.domain.FileUploader.ProgressCallback
import ru.otus.android.finish.MainActivity
import ru.otus.android.utils.NotificationFactory
import ru.otus.android.utils.getPendingIntent
import java.util.concurrent.Executors

class ForegroundService  : Service() {

    private val fileUploader = FileUploader()

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("OTUS: ForegroundService onStartCommand")

        val notificationFactory = NotificationFactory(
            context = this,
            pendingIntent = getPendingIntent(MainActivity::class.java),
        )

        startForeground(
            1,
            notificationFactory.create(title = "Uploading", text = "Starting...")
        )

        Executors.newSingleThreadExecutor().execute {
            fileUploader.upload(object : ProgressCallback {
                override fun onProgress(progress: Int) {
                    println("OTUS: ForegroundService progress $progress%")

                    val notificationManager =
                        getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.notify(
                        1,
                        notificationFactory.create(text = "progress $progress%"),
                    )
                }
            })

            stopSelf()
        }

        return START_STICKY
    }
}
