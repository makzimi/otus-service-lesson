package ru.otus.android.start

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.otus.android.service.databinding.ActivityPingpongBinding

class PingPongActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPingpongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPingpongBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.ping.setOnClickListener {

        }
    }
}
