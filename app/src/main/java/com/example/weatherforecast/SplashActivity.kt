package com.example.weatherforecast

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.VideoView

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    lateinit var video: VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        video=findViewById(R.id.start_video)

        val path = "android.resource://com.example.weatherforecast/"+R.raw.splash_video
        val uri = Uri.parse(path)
        video.setVideoURI(uri)
        video.start()
        video.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = true
        }
    }
}