package com.example.todoapp.Ui.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.Data.DateItem
import com.example.todoapp.R
import com.example.todoapp.databinding.DateLayoutBinding

class DateAdapter(private val dataItem: List<DateItem>,
                  private var selectedPosition: Int,
                  private var selectedDate: String,
                  private val onDateSelected: (String) -> Unit):
    RecyclerView.Adapter<DateAdapter.DateViewHolder>() {

    override fun onCreateViewHolder(
        view: ViewGroup,
        p1: Int
    ): DateViewHolder {
        val binding = DateLayoutBinding.inflate(LayoutInflater.from(view.context),view, false)
        return DateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        val item = dataItem[position]

        holder.binding.tvDate.text = item.date
        holder.binding.tvDay.text = item.dayName

        if (position == selectedPosition) {
            holder.binding.tvDate.setTextColor(
                ContextCompat.getColor(holder.itemView.context, R.color.white)
            )
            holder.binding.tvDay.setTextColor(
                ContextCompat.getColor(holder.itemView.context, R.color.white)
            )
            holder.itemView.setBackgroundResource(R.drawable.selected_item_background)
        } else {
            holder.itemView.setBackgroundResource(R.drawable.date_item_background)
            holder.binding.tvDate.setTextColor(
                ContextCompat.getColor(holder.itemView.context, R.color.black)
            )
            holder.binding.tvDay.setTextColor(
                ContextCompat.getColor(holder.itemView.context, R.color.black)
            )
        }
        holder.itemView.setOnClickListener {

            val oldPosition = selectedPosition

            selectedPosition = holder.bindingAdapterPosition
            selectedDate = item.date
            notifyItemChanged(oldPosition)
            notifyItemChanged(selectedPosition)
            onDateSelected(selectedDate)
        }
    }

    override fun getItemCount(): Int {
        return dataItem.size
    }
    class DateViewHolder(val binding: DateLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}