package ru.otus.android.finish.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import ru.otus.android.domain.FileUploader
import ru.otus.android.domain.FileUploader.ProgressCallback
import java.util.concurrent.Executors

class BackgroundService : Service() {

    private val fileUploader = FileUploader()

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("OTUS: BackgroundService onStartCommand")

        Executors.newSingleThreadExecutor().execute {
            fileUploader.upload(object : ProgressCallback {
                override fun onProgress(progress: Int) {
                    println("OTUS: BackgroundService onProgress $progress%")
                }
            })

            stopSelf()
        }

        return START_STICKY
    }
}
