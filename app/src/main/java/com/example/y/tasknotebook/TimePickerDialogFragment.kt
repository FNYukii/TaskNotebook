package com.example.y.tasknotebook

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class TimePickerDialogFragment: DialogFragment(), TimePickerDialog.OnTimeSetListener {

    //呼び出し元のActivityに値を渡すリスナー
    interface OnSelectedTimeListener {
        fun selectedTime(hour: Int, minute: Int)
    }

    private lateinit var listener: OnSelectedTimeListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnSelectedTimeListener){
            listener = context
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = Date().time
        val context = context
        return when {
            context != null -> {
                TimePickerDialog(
                    context,
                    this,
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                )
            }
            else -> super.onCreateDialog(savedInstanceState)
        }
    }

    //TimePickerDialogから選択結果を受け取る
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        listener.selectedTime(hourOfDay, minute)
    }
}