package com.example.todoapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.Data.DataClass.ChoseDataItem
import com.example.todoapp.Data.DateItem
import com.example.todoapp.Domain.TodoItem
import com.example.todoapp.Ui.Adapter.ChoseActivityAdapter
import com.example.todoapp.Ui.Adapter.DateAdapter
import com.example.todoapp.Ui.Adapter.Viewmodel.GeminiViewModel
import com.example.todoapp.Ui.Adapter.Viewmodel.TodoViewModel
import com.example.todoapp.Ui.Adapter.Viewmodel.TodoViewModelFactory
import com.example.todoapp.Ui.Adapter.interfaces.OnCategoryClickListener
import com.example.todoapp.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.launch
import java.time.LocalDate

class MainActivity : AppCompatActivity(), OnCategoryClickListener {
    //    https://www.behance.net/gallery/183407057/To-Do-List-App-UIUX-App-Design
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: TodoViewModel
    private lateinit var todoFilterList: List<TodoItem>
    private var viewType = 1
    private var gridType = 1
    private var selectedDate = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory = TodoViewModelFactory(this.application)
        viewModel = ViewModelProvider(this,factory)[TodoViewModel::class.java]

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

//        Chose Adapter
        val choseList = listOf(
            ChoseDataItem(R.drawable.ic_bulb, "Idea", "0"),
            ChoseDataItem(R.drawable.ic_food, "Food", "0"),
            ChoseDataItem(R.drawable.ic_board, "Work", "0"),
            ChoseDataItem(R.drawable.ic_gym, "Box", "0"),
            ChoseDataItem(R.drawable.ic_music, "Music", "0"),
        )

        val choseAdapter =
            ChoseActivityAdapter(choseList, emptyList(), viewType,gridType,this)
        if(gridType == 1) {
            binding.activityRvList.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        } else {
            binding.activityRvList.layoutManager =
                GridLayoutManager(this, 2)
        }
        binding.activityRvList.adapter = choseAdapter
        binding.titleBar.backBtn.setOnClickListener {

            gridType = if (gridType == 1) 2 else 1

            if (gridType == 2) {

                binding.activityRvList.layoutManager =
                    GridLayoutManager(this, 2)

            } else {

                binding.activityRvList.layoutManager =
                    LinearLayoutManager(this)
            }

            choseAdapter.setGridLayout(gridType)
        }

        // Observe Todo list
        lifecycleScope.launch {
            viewModel.todoItems.collect { todoList ->
                todoFilterList = todoList

                // Update category counts immediately
                choseAdapter.submitList(todoList, selectedDate)
            }
        }

        selectedDate = dates[0].date

        val adapter = DateAdapter(
            dataItem = dates, 0,
            selectedDate = selectedDate
        ) { date ->

            selectedDate = date

            // Recalculate category counts for selected date
            choseAdapter.submitList(todoFilterList, selectedDate)
        }

        binding.rvDate.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvDate.adapter = adapter
        binding.floatingBtn.setOnClickListener {
            startActivity(Intent(this, CreateTaskActivity::class.java))
        }
        binding.titleBar.clockBtn.visibility = View.GONE
        binding.titleBar.backBtn.setBackgroundResource(R.drawable.ic_box_icon)
        binding.titleBar.backBtn.visibility = View.VISIBLE

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onCategoryClick(category: String) {
        val intent = Intent(
            this,
            SingleTaskActivity::class.java
        )
        intent.putExtra("selected_date", selectedDate)
        intent.putExtra("selected_category", category)
        startActivity(intent)
    }
    override fun onDeleteTodo(todo: List<TodoItem>) {
        TODO("Not yet implemented")
    }
}