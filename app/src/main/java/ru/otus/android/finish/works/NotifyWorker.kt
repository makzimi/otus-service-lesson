package ru.otus.android.finish.works

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotifyWorker(
    context: Context,
    workerParams: WorkerParameters,
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        println("OTUS: NotifyWorker NotifyWorker doWork")
        return Result.success()
    }
}
