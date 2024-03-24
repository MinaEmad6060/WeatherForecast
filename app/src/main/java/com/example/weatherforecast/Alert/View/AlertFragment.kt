package com.example.weatherforecast.Alert.View

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentAlertBinding
import java.util.Calendar
import java.util.Random

class AlertFragment : Fragment() {

    private val TAG = "AlertFragment"
    private lateinit var binding: FragmentAlertBinding
    //private var timeInMillis :Long = 0
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



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlertBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDialog()
        onClickFAB()
        onClickStartDate()
        onClickEndDate()
        onClickStartTime()
        onClickEndTime()
        onClickDialogCancel()
        createNotificationChannel()
        onClickDialogOk()
        //convertToMillisecond()
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
            initAlarm()
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

    private fun initAlarm(){
        Toast.makeText(requireActivity(), "Reminder Set!", Toast.LENGTH_SHORT).show()

        val intent = Intent(requireActivity(), Receiver::class.java)
        intent.putExtra("title","iphone")
        val pendingIntent = PendingIntent.getBroadcast(requireActivity(), 0, intent,
            PendingIntent.FLAG_IMMUTABLE)

        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager

//        val timeAtButtonClick = System.currentTimeMillis()
//        Log.i("timeInMillis", "timeInMillis1: $timeAtButtonClick")
//
//        val tenSecondsInMillis = 1000 * 0
//        Log.i("timeInMillis", "timeInMillis2: ${timeAtButtonClick + tenSecondsInMillis }")

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