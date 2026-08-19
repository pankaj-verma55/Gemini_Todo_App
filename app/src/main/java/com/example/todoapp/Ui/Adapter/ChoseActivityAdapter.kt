package com.example.todoapp.Ui.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.CreateTaskActivity
import com.example.todoapp.Data.DataClass.ChoseDataItem
import com.example.todoapp.Domain.TodoItem
import com.example.todoapp.SingleTaskActivity
import com.example.todoapp.Ui.Adapter.interfaces.OnCategoryClickListener
import com.example.todoapp.databinding.ChoseActivityLayoutBinding

class ChoseActivityAdapter(private var itemList: List<ChoseDataItem>,
                                private var todoItemList: List<TodoItem>,
                                private val viewTypes: Int,
                                private val listener: OnCategoryClickListener? = null
) :
    RecyclerView.Adapter<ChoseActivityAdapter.ChoseActivityViewHolder>() {
    override fun onCreateViewHolder(
        view: ViewGroup,
        position: Int
    ): ChoseActivityViewHolder {
        val binding = ChoseActivityLayoutBinding.inflate(
            LayoutInflater.from(view.context), view, false
        )
        return ChoseActivityViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ChoseActivityViewHolder,
        position: Int
    ) {
        if (viewTypes == 1) {
            val item = itemList[position]
            holder.binding.checkbox.visibility = View.GONE
            holder.binding.arrowDown.visibility = View.VISIBLE
            holder.binding.iconImage.visibility = View.VISIBLE
            holder.binding.timeTxt.visibility = View.GONE
            holder.binding.titleTxt.text = item.task
            holder.binding.totalTaskTxt.text = item.totalTask
            holder.binding.root.setOnClickListener {
                listener?.onCategoryClick(item.task)
                val intent = Intent(
                    holder.binding.root.context,
                    SingleTaskActivity::class.java
                )
                intent.putExtra("selected_category", item.task)
                holder.binding.root.context.startActivity(intent)
            }
        } else {

            val item2 = todoItemList[position]

            holder.binding.arrowDown.visibility = View.GONE
            holder.binding.checkbox.visibility = View.VISIBLE
            holder.binding.iconImage.visibility = View.GONE
            holder.binding.timeTxt.visibility = View.VISIBLE
            holder.binding.totalTaskTxt.text = item2.description
            holder.binding.titleTxt.text = item2.title
            holder.binding.root.setOnClickListener {
                holder.binding.checkbox.isChecked = !holder.binding.checkbox.isChecked
            }
        }

    }

    override fun getItemCount(): Int {
        return if(viewTypes==1) {
            itemList.size
        } else {
            todoItemList.size
        }
    }

    fun submitList(
        todoList: List<TodoItem>,
        selectedDate: String
    ) {
        if(viewTypes==2) {
            todoItemList = todoList
        } else {
            itemList = itemList.map { choseItem ->

                val count = todoList.count { todo ->
                    // Takes the first segment before the comma: "19"
                    val todoDay = todo.fullDate.split("/").firstOrNull()?.trim()
                    todo.listSelectedItem == choseItem.task && todoDay == selectedDate
                }

                choseItem.copy(
                    totalTask = count.toString()
                )
            }
        }
        notifyDataSetChanged()
    }

    class ChoseActivityViewHolder(val binding: ChoseActivityLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)
}