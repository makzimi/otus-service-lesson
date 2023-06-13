package ru.otus.android.finish.works

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import ru.otus.android.domain.FileUploader
import ru.otus.android.domain.FileUploader.ProgressCallback

class UploadWorker(
    context: Context,
    workerParams: WorkerParameters,
) : Worker(context, workerParams) {

    private val fileUploader = FileUploader()

    override fun doWork(): Result {
        println("OTUS: UploadWorker doWork")

        fileUploader.upload(object : ProgressCallback {
            override fun onProgress(progress: Int) {
                if (isStopped) {
                    fileUploader.cancel()
                } else {
                    println("OTUS: UploadWorker progress $progress%")
                }
            }
        })

        return Result.success()
    }
}