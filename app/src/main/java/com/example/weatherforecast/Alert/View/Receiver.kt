package com.example.weatherforecast.Alert.View

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.app.Dialog

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
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import com.example.weatherforecast.Alert.ViewModel.AlertFragmentViewModel
import com.example.weatherforecast.Alert.ViewModel.AlertFragmentViewModelFactory
import com.example.weatherforecast.Main.Utils.Companion.radioGroupBtn
import com.example.weatherforecast.Model.Remote.Alert.DataStateAlertRemote
import com.example.weatherforecast.R
import com.example.weatherforecast.di.AppContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Receiver : BroadcastReceiver(){
    private var lat=0.0
    private var lon=0.0
    private var id = "n"
    private lateinit var appContainer: AppContainer
    private lateinit var alertFragmentViewModelFactory: AlertFragmentViewModelFactory
    private lateinit var alertFragmentViewModel: AlertFragmentViewModel
    private lateinit var sharedPreferences: SharedPreferences


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {


//        if (context != null) {
//            CoroutineScope(Dispatchers.IO).launch {
//                alarm(context)
//            }
//        }

        if (context != null) {
            getSharedPreferences(context)
            initViewModel(context)
            //id = AlertFragment.alertWeatherId
            Log.i("alertRemote", "id : $id ")
            alertFragmentViewModel.getAlertWeatherRemoteVM(
                33.44, -94.04, "fbcc4a868720bf2c249f5c6b9561a30b"
            )

            CoroutineScope(Dispatchers.IO).launch {
                alertFragmentViewModel.alertWeatherRemote.collectLatest { value ->
                    when (value) {
                        is DataStateAlertRemote.Success -> {
                            Log.i("receive", "success: ")
                            Log.i("receive", "event:${value.data.alerts[0].event} ")
                            Log.i("receive", "desc:${value.data.alerts[0].description} ")


                            val builder = context?.let {
                                NotificationCompat.Builder(it, "notifyLemubit")
                                    .setSmallIcon(R.drawable.app)
                                    .setContentTitle(value.data.alerts[0].event)
                                    .setContentText(value.data.alerts[0].description)
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
                            if (notificationManager != null && builder != null && radioGroupBtn=="Notification") {
                                notificationManager.notify(200, builder.build())
                            }else if(radioGroupBtn=="Alarm"){
                                CoroutineScope(Dispatchers.IO).launch {
                                    alarm(context,value.data.alerts[0].event,value.data.alerts[0].description)
                                    val mediaPlayer = MediaPlayer.create(context, R.raw.alarm)
                                    mediaPlayer.start()
                                }
                            }
                            Log.i("alertRemote", "alertRemote-Success: ")
                            Log.i("alertRemote", "alertRemote-${value.data.timezone}")
                            Log.i("alertRemote", "alertRemote-${value.data.alerts[0].event}: ")
                            Log.i(
                                "alertRemote",
                                "alertRemote-${value.data.alerts[0].description}: "
                            )
                        }

                        is DataStateAlertRemote.Failure -> {
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
    }

    private fun getSharedPreferences(context: Context)
    {
        sharedPreferences =
            context.getSharedPreferences("locationDetails", Context.MODE_PRIVATE)
        lat = sharedPreferences.getString("latitude", "0")!!.toDouble()
        lon = sharedPreferences.getString("longitude", "0")!!.toDouble()
        radioGroupBtn = sharedPreferences.getString("alarmNotify", "Notification")!!
        id = sharedPreferences.getString("AlertID","0")!!
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("InflateParams")
    private suspend fun alarm(context: Context, event: String, desc: String) {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val floatingLayout =
            LayoutInflater.from(context).inflate(R.layout.item_alarm, null)
        val eventInfo=floatingLayout.findViewById<TextView>(R.id.event_alarm_text)
        val descInfo=floatingLayout.findViewById<TextView>(R.id.desc_alarm_text)
        val btnDismiss = floatingLayout.findViewById<Button>(R.id.dismiss_btn)
        eventInfo.text=event
        descInfo.text=desc
        btnDismiss.setOnClickListener {
            windowManager.removeView(floatingLayout)
        }
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        params.gravity = Gravity.TOP
//                params.x = 0
//                params.y = 100

        // Add the view to the window
        withContext(Dispatchers.Main) {
            // Add the view to the window
            windowManager.addView(floatingLayout, params)
        }
    }


}