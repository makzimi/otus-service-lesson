package ru.otus.android.finish

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType.CONNECTED
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import ru.otus.android.finish.services.BackgroundService
import ru.otus.android.finish.services.BoundService
import ru.otus.android.finish.services.BoundService.UploadBinder
import ru.otus.android.finish.services.ForegroundService
import ru.otus.android.finish.works.NotifyWorker
import ru.otus.android.finish.works.UploadWorker
import ru.otus.android.service.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val connection: ServiceConnection = object: ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            (service as UploadBinder).subscribeToProgress { progress ->
                runOnUiThread {
                    binding.progress.text = "$progress%"
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) { }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.startBackground.setOnClickListener {
            Intent(this, BackgroundService::class.java).also { intent ->
                startService(intent)
            }
        }

        binding.startForeground.setOnClickListener {
            Intent(this, ForegroundService::class.java).also { intent ->
                startForegroundService(intent)
            }
        }

        binding.startBound.setOnClickListener {
            Intent(this, BoundService::class.java).also { intent ->
                startService(intent)
            }
        }

        binding.startWork.setOnClickListener {
            // 1
//            val uploadRequest = OneTimeWorkRequest.from(UploadWorker::class.java)
//            WorkManager.getInstance(this)
//                .enqueue(uploadRequest)

            // 2
//            val uploadRequest = OneTimeWorkRequest.from(UploadWorker::class.java)
//            WorkManager.getInstance(this)
//                .enqueueUniqueWork(
//                    "uploadWork",
//                    ExistingWorkPolicy.REPLACE,
//                    uploadRequest,
//                )

            // 3
            val uploadRequest = OneTimeWorkRequest.from(UploadWorker::class.java)
            val notifyRequest = OneTimeWorkRequestBuilder<NotifyWorker>()
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(CONNECTED)
                        .build()
                )
                .build()

            WorkManager.getInstance(this)
                .beginUniqueWork(
                    "uploadWork",
                    ExistingWorkPolicy.REPLACE,
                    uploadRequest,
                )
                .then(notifyRequest)
                .enqueue()
        }
    }

    override fun onStart() {
        super.onStart()

        Intent(this, BoundService::class.java).also { intent ->
            bindService(intent, connection, BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
    }
}
