package com.vedantpansuriya.mad_assignment_2_21012011059

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.core.app.NotificationCompat
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private lateinit var tasks: ArrayList<Task>
    private lateinit var adapter: ArrayAdapter<Task>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tasks = ArrayList()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, tasks)
        val listView = findViewById<ListView>(R.id.taskList)
        listView.adapter = adapter
        val addButton = findViewById<Button>(R.id.addButton)
        addButton.setOnClickListener { addTask() }
        listView.setOnItemLongClickListener { _, _, position, _ ->
            val taskToRemove = tasks[position]
            tasks.removeAt(position)
            adapter.notifyDataSetChanged()
            true
        }
    }
    private fun addTask() {
        val taskEditText = findViewById<EditText>(R.id.taskInput)
        val taskName = taskEditText.text.toString()
        if (taskName.isNotEmpty()) {
            tasks.add(Task(taskName, false))
            adapter.notifyDataSetChanged()
            taskEditText.text.clear()
        }
    }
    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "my_channel_id"
    }
    private fun setReminderForTask(context: Context, task: Task, reminderTimeMillis: Long) {
        task.reminder = reminderTimeMillis

        // Create an intent that will be triggered when the reminder fires
        val intent = Intent(context, ReminderBroadcastReceiver::class.java)
        intent.putExtra("task_name", task.name)  // Include task name in the intent for notification

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.hashCode(),  // Use a unique ID for each task to distinguish between reminders
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Schedule the reminder using AlarmManager
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTimeMillis, pendingIntent)

        // Display a notification when the reminder fires
        createNotificationChannel(context)
        showNotification(context, task.name)
    }
    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "My Channel"
            val descriptionText = "Channel description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun showNotification(context: Context, taskName: String) {
        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Task Reminder")
            .setContentText("Don't forget: $taskName")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, builder.build())
    }
    private fun showDateTimePicker(task: Task) {
        val calendar = Calendar.getInstance()
        val currentMillis = calendar.timeInMillis

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, day ->
                calendar.set(year, month, day)
                val timePickerDialog = TimePickerDialog(
                    this,
                    { _, hourOfDay, minute ->
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calendar.set(Calendar.MINUTE, minute)
                        val context = this
                        val reminderTimeMillis = calendar.timeInMillis

                        if (reminderTimeMillis > currentMillis) {
                            setReminderForTask(context, task, reminderTimeMillis)
                        } else {

                        }
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false
                )
                timePickerDialog.show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}