package ru.otus.android.domain

class FileUploader {

    interface ProgressCallback {
        fun onProgress(progress: Int)
    }

    private var isWorking = false

    fun upload(progressCallback: ProgressCallback) {
        isWorking = true
        repeat(100) {
            if (!isWorking) return@repeat

            Thread.sleep(100)
            progressCallback.onProgress(it)
        }
    }

    fun cancel() {
        isWorking = false
    }
}