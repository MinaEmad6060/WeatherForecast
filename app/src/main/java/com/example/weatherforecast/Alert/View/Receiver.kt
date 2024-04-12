package com.example.weatherforecast.Alert.View

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.example.weatherforecast.Alert.ViewModel.AlertFragmentViewModel
import com.example.weatherforecast.Alert.ViewModel.AlertFragmentViewModelFactory
import com.example.weatherforecast.Home.ViewModel.HomeFragmentViewModel
import com.example.weatherforecast.Home.ViewModel.HomeFragmentViewModelFactory
import com.example.weatherforecast.Main.Utils.Companion.key
import com.example.weatherforecast.Main.Utils.Companion.language
import com.example.weatherforecast.Main.Utils.Companion.lat
import com.example.weatherforecast.Main.Utils.Companion.lon
import com.example.weatherforecast.Main.Utils.Companion.radioGroupBtn
import com.example.weatherforecast.Model.Remote.Alert.DataStateAlertRemote
import com.example.weatherforecast.Model.Remote.Home.DataStateHomeRemote
import com.example.weatherforecast.R
import com.example.weatherforecast.di.AppContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Receiver : BroadcastReceiver(){
    private var id = "n"
    private lateinit var appContainer: AppContainer
    private lateinit var alertFragmentViewModelFactory: AlertFragmentViewModelFactory
    private lateinit var homeFragmentViewModelFactory: HomeFragmentViewModelFactory
    private lateinit var alertFragmentViewModel: AlertFragmentViewModel
    private lateinit var homeFragmentViewModel: HomeFragmentViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var mediaPlayer : MediaPlayer

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        mediaPlayer = MediaPlayer.create(context, R.raw.alarm)

        if (context != null) {
            getSharedPreferences(context)
            initViewModel(context)

            homeFragmentViewModel.getAdditionalWeatherRemoteVM(
                lat, lon, key,"", language,40
            )

            CoroutineScope(Dispatchers.IO).launch {
                homeFragmentViewModel.additionalWeatherList.collectLatest { value ->
                    when (value) {
                        is DataStateHomeRemote.Success -> {

                            val builder = context?.let {
                                NotificationCompat.Builder(it, "notificationChannel")
                                    .setSmallIcon(R.drawable.app)
                                    .setContentTitle(value.data.city.name)
                                    .setContentText(value.data.list[0].weather[0].description)
                                    .setPriority(NotificationCompat.PRIORITY_MAX)
                            }


                            val notificationManager =
                                context?.let { NotificationManagerCompat.from(it) }

                            alertFragmentViewModel.deleteAlertWeatherVM(id)

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
                            Log.i("notifyAlarm", "radioGroupBtn: $radioGroupBtn")
                            if (notificationManager != null && builder != null && radioGroupBtn=="Notification") {
                                notificationManager.notify(200, builder.build())
                            }else if(radioGroupBtn=="Alarm"){
                                CoroutineScope(Dispatchers.IO).launch {
                                    alarm(context,value.data.city.name,value.data.list[0].weather[0].description)
                                    mediaPlayer.start()
                                }
                            }
                            Log.i("alertRemote", "alertRemote-Success: ")
                            Log.i("alertRemote", "alertRemote-${value.data.list[0].weather[0].description}")
                            Log.i("alertRemote", "alertRemote-${value.data.city.name}: ")
                            Log.i(
                                "alertRemote",
                                "alertRemote-${value.data.list[0].weather[0].description}: "
                            )
                        }

                        is DataStateHomeRemote.Failure -> {
                            Log.i("receive", "fail: ")
                            Log.i("alertRemote", "alertRemote-fail: ")
                        }

                        else -> {
                            Log.i("receive", "loading: ")
                            Log.i("alertRemote", "alertRemote-loading: ")
                        }
                    }
                }
            }
        }
    }


    fun initViewModel(context: Context){
        appContainer = AppContainer(context.applicationContext)
        alertFragmentViewModelFactory = appContainer.alertFactory
        alertFragmentViewModel =
            ViewModelProvider(ViewModelStore(), alertFragmentViewModelFactory)
                .get(AlertFragmentViewModel::class.java)
        homeFragmentViewModelFactory = appContainer.homeFactory
        homeFragmentViewModel =
            ViewModelProvider(ViewModelStore(), homeFragmentViewModelFactory)
                .get(HomeFragmentViewModel::class.java)
    }

    private fun getSharedPreferences(context: Context)
    {
        sharedPreferences =
            context.getSharedPreferences("locationDetails", Context.MODE_PRIVATE)
        lat = sharedPreferences.getString("latitude", "0")!!.toDouble()
        lon = sharedPreferences.getString("longitude", "0")!!.toDouble()
        id = sharedPreferences.getString("AlertID","0")!!
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("InflateParams", "SetTextI18n")
    private suspend fun alarm(context: Context, event: String, desc: String) {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val floatingLayout =
            LayoutInflater.from(context).inflate(R.layout.item_alarm, null)
        val eventInfo=floatingLayout.findViewById<TextView>(R.id.event_alarm_text)
        val descInfo=floatingLayout.findViewById<TextView>(R.id.desc_alarm_text)
        val btnDismiss = floatingLayout.findViewById<Button>(R.id.dismiss_btn)
        eventInfo.text=event
        descInfo.text= "Weather Status: $desc"
        btnDismiss.setOnClickListener {
            windowManager.removeView(floatingLayout)
            mediaPlayer.stop()
        }
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        params.gravity = Gravity.TOP

        withContext(Dispatchers.Main) {
            windowManager.addView(floatingLayout, params)
        }
    }


}