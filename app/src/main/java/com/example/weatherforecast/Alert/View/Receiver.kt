package com.example.weatherforecast.Alert.View

import android.Manifest
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import com.example.weatherforecast.Alert.View.AlertFragment.Companion.alertFragmentViewModel
import com.example.weatherforecast.Alert.ViewModel.AlertFragmentViewModel
import com.example.weatherforecast.Alert.ViewModel.AlertFragmentViewModelFactory
import com.example.weatherforecast.Model.Remote.Alert.DataStateAlertRemote
import com.example.weatherforecast.Model.Repo.WeatherRepository
import com.example.weatherforecast.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class Receiver : BroadcastReceiver(){

    private var id = "n"

    override fun onReceive(context: Context?, intent: Intent?) {

        id = intent?.getStringExtra("myData") ?: "not found"
        Log.i("alertRemote", "id : $id ")

        val application = context?.applicationContext as Application


        alertFragmentViewModel.getAlertWeatherRemoteVM(
            33.44, -94.04, "fbcc4a868720bf2c249f5c6b9561a30b"
        )

        CoroutineScope(Dispatchers.IO).launch {
            alertFragmentViewModel.alertWeatherRemote.collectLatest { value ->
                when (value) {
                    is DataStateAlertRemote.Success -> {
                        val builder = context?.let {
                            NotificationCompat.Builder(it, "notifyLemubit")
                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setContentTitle(value.data.alerts[0].event)
                                .setContentText(value.data.alerts[0].description)
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
                            return@collectLatest
                        }
                        if (notificationManager != null && builder != null){
                            notificationManager.notify(200, builder.build())
                        }
                        Log.i("alertRemote", "alertRemote-Success: ")
                        Log.i("alertRemote", "alertRemote-${value.data.timezone}")
                        Log.i("alertRemote", "alertRemote-${value.data.alerts[0].event}: ")
                        Log.i("alertRemote", "alertRemote-${value.data.alerts[0].description}: ")
                    }

                    is DataStateAlertRemote.Failure -> {
                        Log.i("alertRemote", "alertRemote-fail: ")
                    }

                    else -> Log.i("alertRemote", "alertRemote-loading: ")
                }
            }
        }


        alertFragmentViewModel.deleteAlertWeatherVM(id,application)


    }

}