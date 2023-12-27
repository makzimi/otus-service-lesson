package ru.otus.android.start

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.otus.android.service.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.startBackground.setOnClickListener { }

        binding.startForeground.setOnClickListener { }

        binding.startBound.setOnClickListener { }

        binding.tryIPC.setOnClickListener {
            startActivity(Intent(this, PingPongActivity::class.java))
        }

        binding.startWork.setOnClickListener { }
    }
}
