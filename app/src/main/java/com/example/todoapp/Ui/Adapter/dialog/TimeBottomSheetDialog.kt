package com.example.todoapp.Ui.Adapter.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import com.example.todoapp.databinding.TimeBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

@SuppressLint("DefaultLocale")
class TimeBottomSheetDialog(context: Context,
                            private val onTimeSelected: (String) -> Unit) :
    BottomSheetDialog(context) {
    private val binding =
        TimeBottomSheetBinding.inflate(layoutInflater)

    init {
        setContentView(binding.root)
        binding.timePicker.setIs24HourView(true)
        Log.d(
            "TimePicker",
            "24 hour = ${binding.timePicker.is24HourView}"
        )



        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        binding.saveButton.setOnClickListener {

            val hour = binding.timePicker.hour
            val minute = binding.timePicker.minute

            val selectedTime = String.format(
                "%02d:%02d",
                hour,
                minute
            )
            onTimeSelected(selectedTime)
            Log.d("TimePicker", "Selected time: $selectedTime")

            dismiss()
        }
    }
}

//    override fun onStart() {
//        super.onStart()
//
//        val bottomSheet =
//            findViewById<View>(
//                com.google.android.material.R.id.design_bottom_sheet
//            )
//
//        bottomSheet?.let { sheet ->
//            val screenHeight = context.resources.displayMetrics.heightPixels
//            sheet.layoutParams.height = (screenHeight * 0.7).toInt()
//            sheet.requestLayout()
//        }
//    }