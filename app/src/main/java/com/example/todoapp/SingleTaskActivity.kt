package com.example.todoapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.Data.DataClass.ChoseDataItem
import com.example.todoapp.Data.DateItem
import com.example.todoapp.Domain.TodoItem
import com.example.todoapp.Ui.Adapter.ChoseActivityAdapter
import com.example.todoapp.Ui.Adapter.DateAdapter
import com.example.todoapp.Ui.Adapter.Viewmodel.TodoViewModel
import com.example.todoapp.Ui.Adapter.interfaces.OnCategoryClickListener
import com.example.todoapp.databinding.ActivitySingleTaskBinding
import kotlinx.coroutines.launch
import java.time.LocalDate

class SingleTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySingleTaskBinding
    private lateinit var viewModel: TodoViewModel
    private var todoFilterList: List<TodoItem> = emptyList()
    private var currentSelectedDate: String = ""
    private var viewType = 2

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySingleTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[TodoViewModel::class.java]
        //        Date Adapter
        val today = LocalDate.now()
        val dates = (0 until 31).map { index ->
            val date = today.plusDays(index.toLong())
            DateItem(
                date = date.dayOfMonth.toString(),
                dayName = date.dayOfWeek.name
                    .lowercase()
                    .replaceFirstChar { it.uppercase() },
                localDate = date
            )
        }
        currentSelectedDate = dates[0].date
        val selectedCategory = intent.getStringExtra("selected_category") ?: ""


//        Chose Adapter
        val choseList = listOf(
            ChoseDataItem(R.drawable.ic_bulb, "Idea", "0"),
            ChoseDataItem(R.drawable.ic_food, "Food", "0"),
            ChoseDataItem(R.drawable.ic_board, "Work", "0"),
            ChoseDataItem(R.drawable.ic_gym, "Box", "0"),
            ChoseDataItem(R.drawable.ic_music, "Music", "0"),
        )


        val choseAdapter = ChoseActivityAdapter(
            choseList,
            emptyList(), viewType, null
        )


        binding.activityRvList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.activityRvList.adapter = choseAdapter

        fun updateUI() {
            val filteredList = todoFilterList.filter { todo ->
                val todoDay = todo.fullDate
                    .split("/")
                    .firstOrNull()
                    ?.trim()
                println(
                    "FILTERED SIZE-->: category=${todo.listSelectedItem}, " +
                            "selectedCategory=$selectedCategory, " +
                            "todoDay=$todoDay, " +
                            "currentDate=$currentSelectedDate"
                )
                todo.listSelectedItem == selectedCategory &&
                        todoDay == currentSelectedDate
            }

            println("FILTERED SIZE--> = ${filteredList.size}")

            binding.totalDayTask.text = filteredList.size.toString()

            choseAdapter.submitList(
                filteredList,
                currentSelectedDate
            )
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.todoItems.collect { todoList ->
                    println("FILTERED SIZE-->: ${todoList.size}")

                    todoList.forEach {
                        println(
                            "FILTERED SIZE-->: ${it.title} | " +
                                    "${it.listSelectedItem} | " +
                                    "${it.fullDate}"
                        )
                    }
                    todoFilterList = todoList
                    updateUI()
                }
            }
        }

        val adapter = DateAdapter(
            dataItem = dates, 0,
            selectedDate = currentSelectedDate
        ) { selectedDate ->
            currentSelectedDate = selectedDate

            updateUI()
        }

        binding.rvDate.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvDate.adapter = adapter

        binding.titleBar.backBtn.setOnClickListener {
            finish()
        }

        binding.AddTask.setOnClickListener {
            startActivity(Intent(this, CreateTaskActivity::class.java))
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}