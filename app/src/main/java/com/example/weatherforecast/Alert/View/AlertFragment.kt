package com.example.weatherforecast.Alert.View

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.weatherforecast.Alert.ViewModel.AlertFragmentViewModel
import com.example.weatherforecast.Alert.ViewModel.AlertFragmentViewModelFactory
import com.example.weatherforecast.Home.ViewModel.HomeFragmentViewModel
import com.example.weatherforecast.Home.ViewModel.HomeFragmentViewModelFactory
import com.example.weatherforecast.Model.Local.Alert.AlertCalendar
import com.example.weatherforecast.Model.Local.Alert.AlertWeatherDAO
import com.example.weatherforecast.Model.Local.Alert.DataStateAlertRoom
import com.example.weatherforecast.Model.Remote.Alert.DataStateAlertRemote
import com.example.weatherforecast.Model.Remote.Home.DataStateHomeRemote
import com.example.weatherforecast.Model.Repo.Alert.AlertRepo
import com.example.weatherforecast.Model.Repo.Home.HomeRepo
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentAlertBinding
import com.example.weatherforecast.di.AppContainer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Random

class AlertFragment : Fragment() {

    private val TAG = "AlertFragment"
    private lateinit var binding: FragmentAlertBinding
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    private var lat=0.0
    private var lon=0.0
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var dialogAlert: Dialog
    private lateinit var startDateEditText: TextView
    private lateinit var startDateAlert: String
    private lateinit var endDateEditText: TextView
    private lateinit var endDateAlert: String
    private lateinit var startTimeEditText: TextView
    private lateinit var startTimeAlert: String
    private lateinit var endTimeEditText: TextView
    private lateinit var endTimeAlert: String
    private lateinit var btnOk : Button
    private lateinit var btnCancel : Button
    private var startChoice: Long = 0
    private var endChoice: Long = 0

    private lateinit var homeFragmentViewModelFactory: HomeFragmentViewModelFactory
    private lateinit var homeFragmentViewModel: HomeFragmentViewModel

    private lateinit var alertFragmentViewModelFactory: AlertFragmentViewModelFactory
    private lateinit var alertFragmentViewModel: AlertFragmentViewModel
    companion object {
        var alertWeatherId: String=""
    }

    private lateinit var alertAdapter: AlertFragmentAdapter
    private lateinit var alertLayoutManager: LayoutManager
    private lateinit var appContainer: AppContainer

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appContainer = AppContainer(context.applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlertBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getSharedPreferences()
        initDialog()
        onClickFAB()
        onClickStartDate()
        onClickEndDate()
        onClickStartTime()
        onClickEndTime()
        onClickDialogCancel()
        createNotificationChannel()
        onClickDialogOk()
        initHomeViewModel()
        initAlertViewModel()
        setAlertAdapter()
        onClickAlarmRadioButtons()
        alertFragmentViewModel.getAlertWeatherVM()

        Log.i("alertLatLon", "lat: $lat lon: $lon")

        //33.44//-94.04

        lifecycleScope.launch {
            alertFragmentViewModel.alertWeatherRoom.collectLatest { value ->
                when(value){
                    is DataStateAlertRoom.Success -> {
                        Log.i("alert", "favWeather-success:")
                        if (value.data.isNotEmpty()){
                            binding.alarmOffImg.visibility=View.GONE
                            binding.alertRecyclerView.visibility=View.VISIBLE
                        }
                        alertAdapter.submitList(value.data)
                    }
                    is DataStateAlertRoom.Failure -> {Log.i(TAG, "favWeather-fail: ")}
                    else -> Log.i("alert", "favWeather-loading: ")
                }
            }
        }

    }

    private fun onClickAlarmRadioButtons(){
        val radioGroup = dialogAlert.findViewById<RadioGroup>(R.id.radio_group_alarm)
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = group.findViewById<RadioButton>(checkedId)
            if(radioButton.text=="Notification"){
                Log.i("alarmNotify", "Notification")
            }else if(radioButton.text=="Alarm"){
                Log.i("alarmNotify", "Alarm")
            }
        }
    }

    private fun getSharedPreferences()
    {
        sharedPreferences =
            requireActivity().getSharedPreferences("locationDetails", Context.MODE_PRIVATE)
        lat = sharedPreferences.getString("latitude", "0")!!.toDouble()
        lon = sharedPreferences.getString("longitude", "0")!!.toDouble()
        editor = sharedPreferences.edit()
        editor.putString("SelectedFragment","Fav")
        editor.apply()
    }

    private fun setAlertAdapter(){
        alertAdapter = AlertFragmentAdapter { delAlertWeather ->
            showAlert(
                "Delete Alert Item", "Are you sure you want delete this Alert?",
                delAlertWeather, requireActivity()
            )
        }

        alertLayoutManager = LinearLayoutManager(requireActivity(),
            RecyclerView.VERTICAL, false)
        binding.alertRecyclerView.apply {
            adapter = alertAdapter
            layoutManager = alertLayoutManager
        }
    }

    private fun showAlert(title: String, message: String, alertCalendar: AlertCalendar, context: Context):Int {
        val builder = AlertDialog.Builder(context)
        var result =0
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, which ->
            lifecycleScope.launch {
                result= alertFragmentViewModel.deleteAlertWeatherVM(alertCalendar.infoOfAlert)
            }
        }

        builder.setNegativeButton("Cancel") { dialog, which ->
            alarmManager.cancel(pendingIntent);
        }

        builder.show()
        return result
    }

    private fun saveAlertToRoom() :AlertCalendar {
        val alertCalendar= AlertCalendar()
        alertCalendar.infoOfAlert=startDateAlert+","+
                startTimeAlert+","+
                endDateAlert+","+
                endTimeAlert
        lifecycleScope.launch {
            alertFragmentViewModel.insertAlertWeatherVM(alertCalendar)
        }
        Log.i("alertRemote", "saveAlertToRoom : ${alertCalendar.infoOfAlert} ")
        return alertCalendar
    }

    private fun initHomeViewModel() {
        homeFragmentViewModelFactory = appContainer.homeFactory
        homeFragmentViewModel =
            ViewModelProvider(this, homeFragmentViewModelFactory)
                .get(HomeFragmentViewModel::class.java)
    }

    private fun initAlertViewModel() {
        alertFragmentViewModelFactory = appContainer.alertFactory
        alertFragmentViewModel =
            ViewModelProvider(this, alertFragmentViewModelFactory)
                .get(AlertFragmentViewModel::class.java)
    }

    private fun onClickFAB(){
        binding.alertFab.setOnClickListener{
            Log.i("fab", "onClickFAB: ")
            dialogAlert.show()
        }
    }
    private fun onClickDialogOk(){
        btnOk=dialogAlert.findViewById(R.id.dialog_ok)
        btnOk.setOnClickListener{
            val alertCalendar=saveAlertToRoom()
            Log.i("alertRemote", "onClickDialogOk : ${alertCalendar.infoOfAlert} ")
            initAlarm(alertCalendar)
            dialogAlert.cancel()
        }
    }
    private fun onClickDialogCancel(){
        btnCancel=dialogAlert.findViewById(R.id.dialog_cancel)
        btnCancel.setOnClickListener{
            dialogAlert.cancel()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Channel Name"
            val descriptionText = "Channel Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("notifyLemubit", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun initAlarm(alertCalendar: AlertCalendar){
        Toast.makeText(requireActivity(), "Reminder Set!", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireActivity(), Receiver::class.java)
        alertWeatherId=alertCalendar.infoOfAlert
        editor.putString("AlertID",alertWeatherId)
        editor.apply()
//        intent.putExtra("myId", alertCalendar.infoOfAlert)
        Log.i("alertRemote", "initAlarm : $alertWeatherId ")
        pendingIntent = PendingIntent.getBroadcast(requireActivity(), 0, intent,
            PendingIntent.FLAG_IMMUTABLE)

        alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        startChoice=convertToMillisecond(startDateAlert,startTimeAlert)
        endChoice=convertToMillisecond(endDateAlert,endTimeAlert)
        val diff=endChoice-startChoice

        val random = Random()
        val randomNumberBetweenStartAndEnd = (random.nextInt(diff.toInt()) + 1)
        Log.i("initAlarm", "initAlarm: $randomNumberBetweenStartAndEnd")

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            startChoice + randomNumberBetweenStartAndEnd,
            pendingIntent)
    }


    private fun convertToMillisecond(dateString: String, timeString: String): Long{
        //val dateString = "24/3/2024"
        val dateParts = dateString.split("/")
        val day = dateParts[0].toInt()
        val month = dateParts[1].toInt() - 1 // Month in Calendar starts from 0 (January is 0)
        val year = dateParts[2].toInt()

        // Parse the time string into hours and minutes
        //val timeString = "00:40"
        val timeParts = timeString.split(":")
        val hour = timeParts[0].toInt()
        val minute = timeParts[1].toInt()

        // Create a Calendar instance and set the date and time
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute)

        // Get the time in milliseconds
        val timeInMillis = calendar.timeInMillis
        Log.i("timeInMillis", "timeInMillis3: $timeInMillis ")
        return timeInMillis
    }

    private fun initDialog(){
        dialogAlert= Dialog(requireActivity())
        dialogAlert.setContentView(R.layout.dialog_alert)
        dialogAlert.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialogAlert.window?.setBackgroundDrawable(
            ContextCompat.getDrawable(requireActivity(), R.drawable.dialog_bg)
        )
        dialogAlert.setCancelable(false)
    }


    private fun onClickStartDate(){
        startDateEditText= dialogAlert.findViewById(R.id.dialog_start_date_content)
        startDateEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(requireActivity(), { _, year, month, day ->
                startDateAlert = "${day}/${month + 1}/${year}"
                startDateEditText.setText(startDateAlert)
            }, year, month, day)
            datePickerDialog.show()
        }
    }

    private fun onClickEndDate(){
        endDateEditText= dialogAlert.findViewById(R.id.dialog_end_date_content)
        endDateEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(requireActivity(), { _, year, month, day ->
                endDateAlert = "${day}/${month + 1}/${year}"
                endDateEditText.setText(endDateAlert)
            }, year, month, day)
            datePickerDialog.show()
        }
    }

    private fun onClickStartTime() {
        startTimeEditText = dialogAlert.findViewById(R.id.dialog_start_time_content)
        startTimeEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(requireActivity(), { _, hour, minute ->
                // Update the EditText with the selected time
                startTimeAlert = "${String.format("%02d", hour)}:${String.format("%02d", minute)}"
                startTimeEditText.setText(startTimeAlert)
            }, hour, minute, true)

            timePickerDialog.show()
        }
    }

    private fun onClickEndTime() {
        endTimeEditText = dialogAlert.findViewById(R.id.dialog_end_time_content)
        endTimeEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(requireActivity(), { _, hour, minute ->
                // Update the EditText with the selected time
                endTimeAlert = "${String.format("%02d", hour)}:${String.format("%02d", minute)}"
                endTimeEditText.setText(endTimeAlert)
            }, hour, minute, true)
            timePickerDialog.show()
        }
    }
}