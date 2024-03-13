package com.example.weatherforecast

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.weatherforecast.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val path = "android.resource://com.example.weatherforecast/"+R.raw.splash_video
        val uri = Uri.parse(path)
        binding.startVideo.setVideoURI(uri)
        binding.startVideo.start()
        binding.startVideo.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = true
        }


        binding.btnStart.setOnClickListener {
            startActivity(
                Intent(
                    this@SplashActivity,
                    MainActivity::class.java
                )
            )
        }
    }
}