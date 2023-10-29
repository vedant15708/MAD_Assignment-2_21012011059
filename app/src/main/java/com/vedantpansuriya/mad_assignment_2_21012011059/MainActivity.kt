package com.vedantpansuriya.mad_assignment_2_21012011059

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var tasks: ArrayList<Task>
    private lateinit var adapter: ArrayAdapter<Task>
    private lateinit var taskEditText: EditText
    private lateinit var prioritySpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tasks = ArrayList()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, tasks)

        val listView = findViewById<ListView>(R.id.taskList)
        listView.adapter = adapter

        taskEditText = findViewById(R.id.taskInput)
        prioritySpinner = findViewById(R.id.prioritySpinner)


        val priorityOptions = resources.getStringArray(R.array.priority_options)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, priorityOptions)
        prioritySpinner.adapter = spinnerAdapter

        val addButton = findViewById<Button>(R.id.addButton)
        addButton.setOnClickListener { addTask() }

        listView.setOnItemLongClickListener { _, _, position, _ ->
            removeTask(position)
            true
        }
    }

    private fun addTask() {
        val taskName = taskEditText.text.toString()
        val selectedPriority = prioritySpinner.selectedItem.toString()
        val priorityInt = mapPriorityToInt(selectedPriority)

        if (taskName.isNotEmpty()) {
            val newTask = Task(taskName, false, priorityInt)

            var insertIndex = 0
            while (insertIndex < tasks.size && tasks[insertIndex].priority >= priorityInt) {
                insertIndex++
            }

            tasks.add(insertIndex, newTask)
            adapter.notifyDataSetChanged()
            taskEditText.text.clear()
        }
    }
    private fun removeTask(position: Int) {
        tasks.removeAt(position)
        adapter.notifyDataSetChanged()
    }
    private fun mapPriorityToInt(priority: String): Int {
        return when (priority) {
            "High" -> 2
            "Medium" -> 1
            "Low" -> 0
            else -> 1
        }
    }
}