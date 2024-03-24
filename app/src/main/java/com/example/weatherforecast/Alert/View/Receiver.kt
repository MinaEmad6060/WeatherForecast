package com.example.weatherforecast.Alert.View

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.weatherforecast.R

class Receiver : BroadcastReceiver(){

    private var title = ""
    override fun onReceive(context: Context?, intent: Intent?) {



        title = intent?.getStringExtra("title") ?: "none"
        val builder = context?.let {
            NotificationCompat.Builder(it, "notifyLemubit")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText("Hey students, this is a soft reminder...")
                .setPriority(NotificationCompat.PRIORITY_MAX)
        }

        val notificationManager = context?.let { NotificationManagerCompat.from(it) }

        if (context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            } != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        if (notificationManager != null && builder != null){
            notificationManager.notify(200, builder.build())
        }
    }
}