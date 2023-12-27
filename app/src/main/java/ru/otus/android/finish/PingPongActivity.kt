package ru.otus.android.finish

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ru.otus.android.finish.services.SeparateService
import ru.otus.android.service.databinding.ActivityMainBinding
import ru.otus.android.service.databinding.ActivityPingpongBinding
import kotlin.random.Random

class PingPongActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPingpongBinding

    private val connection: ServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

            mService = Messenger(service);

            try {
                val msg: Message = Message.obtain(null, SeparateService.MSG_REGISTER_CLIENT)
                msg.replyTo = mMessenger
                mService!!.send(msg)
            } catch (e: RemoteException) {
                Log.e("PingPongActivity", "Can not send message to serivce")
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {}
    }


    var mService: Messenger? = null

    val mMessenger = Messenger(
        IncomingHandler(
            onMessage = { message ->
                binding.history.text = "${binding.history.text}\nPong: $message"
            }
        )
    )

    class IncomingHandler(
        val onMessage: (String) -> Unit,
    ) : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                SeparateService.MSG_PONG -> onMessage("${msg.arg1}")
                else -> super.handleMessage(msg)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPingpongBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.ping.setOnClickListener {
            val pingValue = Random.Default.nextInt(100)
            val msg: Message = Message.obtain(
                null,
                SeparateService.MSG_PING,
                pingValue,
                0,
            )
            msg.replyTo = mMessenger
            binding.history.text = "${binding.history.text}\nPing: $pingValue"

            mService?.send(msg)
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, SeparateService::class.java).also { intent ->
            bindService(intent, connection, BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        mService = null
    }
}