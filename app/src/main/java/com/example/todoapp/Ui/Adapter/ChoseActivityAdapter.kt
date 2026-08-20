package com.example.todoapp.Ui.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.Data.DataClass.ChoseDataItem
import com.example.todoapp.Domain.TodoItem
import com.example.todoapp.R
import com.example.todoapp.SingleTaskActivity
import com.example.todoapp.Ui.Adapter.interfaces.OnCategoryClickListener
import com.example.todoapp.databinding.ChoseActivityLayoutBinding
import com.example.todoapp.databinding.ChoseGridLayoutBinding

class ChoseActivityAdapter(private var itemList: List<ChoseDataItem>,
                           private var todoItemList: List<TodoItem>,
                           private var viewTypes: Int,
                           private var gridType:Int,
                           private val listener: OnCategoryClickListener? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val selectedItems = mutableSetOf<TodoItem>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        return when (viewType) {

            1 -> {
                val binding = ChoseActivityLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                ChoseActivityViewHolder(binding)
            }

            2 -> {
                val binding = ChoseGridLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                TodoGridViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Unknown viewType: $viewType")
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {

        if (viewTypes == 1) {
            // CATEGORY
            val item = itemList[position]
            when (holder) {
                is ChoseActivityViewHolder -> {
                    holder.binding.checkbox.visibility = View.GONE
                    holder.binding.arrowDown.visibility = View.VISIBLE
                    holder.binding.iconImage.visibility = View.VISIBLE
                    holder.binding.timeTxt.visibility = View.GONE
                    holder.binding.iconImage.setImageResource(item.image)
                    holder.binding.titleTxt.text = item.task
                    holder.binding.totalTaskTxt.text =
                        "${item.totalTask} Task"

                    holder.binding.root.setOnClickListener {
                        listener?.onCategoryClick(item.task)
                    }
                }

                is TodoGridViewHolder -> {
                    holder.binding.titleTxt.text = item.task
                    holder.binding.totalTaskTxt.text =
                        "${item.totalTask} Task"
                    holder.binding.imageGridIcon.setImageResource(item.image)

                    holder.binding.root.setOnClickListener {
                        listener?.onCategoryClick(item.task)
                    }
                }
            }

        } else {
            val item = todoItemList[position]
            when (holder) {
                is ChoseActivityViewHolder -> {
                    holder.binding.arrowDown.visibility = View.GONE
                    holder.binding.checkbox.visibility = View.VISIBLE
                    holder.binding.iconImage.visibility = View.GONE
                    holder.binding.timeTxt.visibility = View.VISIBLE
                    holder.binding.timeTxt.text = item.time
                    holder.binding.titleTxt.text = item.title
                    holder.binding.totalTaskTxt.text = item.description
                    holder.binding.checkbox.isChecked =
                        selectedItems.contains(item)
                    holder.binding.checkbox.setOnClickListener {

                        if (holder.binding.checkbox.isChecked) {
                            selectedItems.add(item)
                        } else {
                            selectedItems.remove(item)
                        }

                        listener?.onDeleteTodo(
                            selectedItems.toList()
                        )
                    }
                }
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

            selectedItems.retainAll(todoItemList.toSet())
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

    fun setGridLayout(layout: Int) {
        gridType = layout
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (gridType == 1) {
            1 // List
        } else {
            2 // Grid
        }
    }
class ChoseActivityViewHolder(
    val binding: ChoseActivityLayoutBinding
) : RecyclerView.ViewHolder(binding.root)

    class TodoGridViewHolder(
        val binding: ChoseGridLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root)
}