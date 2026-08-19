package com.example.todoapp

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todoapp.databinding.ActivityCreateTaskBinding
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.Data.DataClass.ChoseItem
import com.example.todoapp.Ui.Adapter.ChoseItemAdapter
import com.example.todoapp.Ui.Adapter.Viewmodel.TodoViewModel
import kotlinx.coroutines.launch
import java.util.Calendar

class CreateTaskActivity : AppCompatActivity() {
    private lateinit var viewModel: TodoViewModel
    private lateinit var binding: ActivityCreateTaskBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTaskBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[TodoViewModel::class.java]
        val choseList = listOf(
            ChoseItem(title = "Idea"),
            ChoseItem(title = "Food"),
            ChoseItem(title = "Work"),
            ChoseItem(title = "Box"),
            ChoseItem(title = "Music")
        )
        val adapter = ChoseItemAdapter(choseList, onItemClick = { selectedItem ->
            binding.titleTxt.text = selectedItem.title
            binding.rvItem.visibility = View.GONE
        })
        binding.rvItem.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvItem.adapter = adapter


        binding.titleBar.backBtn.setOnClickListener {
            finish()
        }

        binding.titleBar.clockBtn.setOnClickListener {
            binding.calenderView.visibility =
                if (binding.calenderView.isVisible) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
        }

        binding.layoutItem.setOnClickListener {
            binding.rvItem.visibility =
                if (binding.rvItem.isVisible) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            binding.arrowDown.rotation = if (binding.arrowDown.rotation == 0f) 180f else 0f
        }
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = binding.calenderView.date

        var date = "${calendar.get(Calendar.DAY_OF_MONTH)}/" +
                "${calendar.get(Calendar.MONTH) + 1}/" +
                "${calendar.get(Calendar.YEAR)}"
        binding.calenderView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            date = "$dayOfMonth/${month + 1}/$year"
            println(date)
        }
        binding.createTaskBtn.setOnClickListener {
            val selectedItem = binding.titleTxt.text.toString()
            val title = binding.taskTxt.text.toString()
            val description = binding.taskDescriptionTxt.text.toString()

            if (title.isNotBlank()) {
                viewModel.addTodo(
                    date,
                    selectedItem,
                    title = title,
                    description = description,
                    done = false
                )
                finish()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}