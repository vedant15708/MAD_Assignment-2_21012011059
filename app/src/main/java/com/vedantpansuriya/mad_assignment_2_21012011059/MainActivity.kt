package com.vedantpansuriya.mad_assignment_2_21012011059

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView

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
}