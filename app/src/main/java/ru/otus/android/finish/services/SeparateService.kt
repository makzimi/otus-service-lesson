package ru.otus.android.finish.services

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import ru.otus.android.finish.MainActivity
import ru.otus.android.utils.NotificationFactory
import ru.otus.android.utils.getPendingIntent

class SeparateService: Service() {

    companion object {
        const val MSG_REGISTER_CLIENT = 1
        const val MSG_UNREGISTER_CLIENT = 2
        const val MSG_PING = 3
        const val MSG_PONG = 3
    }

    override fun onBind(intent: Intent?): IBinder? {
        return mMessenger.binder
    }

    val mMessenger: Messenger = Messenger(IncomingHandler())

    var mClients = ArrayList<Messenger>()

    var mValue = 0

    inner class IncomingHandler : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_REGISTER_CLIENT -> mClients.add(msg.replyTo)
                MSG_UNREGISTER_CLIENT -> mClients.remove(msg.replyTo)
                MSG_PING -> {
                    mValue = msg.arg1
                    mClients.forEach { nextMessenger ->
                        nextMessenger.send(
                            Message.obtain(null, MSG_PONG, mValue + 1, 0)
                        )
                    }
                }

                else -> super.handleMessage(msg)
            }
        }
    }

    override fun onCreate() {
        val notificationFactory = NotificationFactory(
            context = this,
            pendingIntent = getPendingIntent(MainActivity::class.java),
        )

        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(
            1,
            notificationFactory.create(text = "Separate service started"),
        )
    }

    override fun onDestroy() {
        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.cancel(1)
    }
}