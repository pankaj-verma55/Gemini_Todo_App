package com.example.todoapp.Ui.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.todoapp.R
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.Data.DataClass.ChoseItem
import com.example.todoapp.databinding.ChoseActivityLayoutBinding

class ChoseItemAdapter(
    private var itemList: List<ChoseItem>,
    private val onItemClick: (ChoseItem) -> Unit,
) : RecyclerView.Adapter<ChoseItemAdapter.ChoseItemAdapter>() {
    override fun onCreateViewHolder(
        view: ViewGroup, position: Int
    ): ChoseItemAdapter {
        val binding = ChoseActivityLayoutBinding.inflate(
            LayoutInflater.from(view.context), view, false
        )
        return ChoseItemAdapter(binding)
    }

    override fun onBindViewHolder(
        holder: ChoseItemAdapter, position: Int
    ) {
        val item = itemList[position]
        holder.binding.titleTxt.text = item.title
        holder.binding.iconImage.visibility = View.GONE
        holder.binding.totalTaskTxt.visibility = View.GONE
        holder.binding.arrowDown.visibility = View.GONE
        holder.itemView.background = ContextCompat.getDrawable(
            holder.itemView.context, R.drawable.date_item_background
        )

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ChoseItemAdapter(val binding: ChoseActivityLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)
}