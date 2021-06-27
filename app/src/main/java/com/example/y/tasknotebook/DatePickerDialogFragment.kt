package com.example.y.tasknotebook

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerDialogFragment: DialogFragment(), DatePickerDialog.OnDateSetListener {

    //呼び出し元のActivityに値を渡すリスナー
    interface OnSelectedDateListener {
        fun selectedDate(year: Int, month: Int, date: Int)
    }

    private lateinit var listener: OnSelectedDateListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnSelectedDateListener){
            listener = context
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = Date().time
        val context = context
        return when {
            context != null -> {
                DatePickerDialog(
                    context,
                    this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DATE)
                )
            }
            else -> super.onCreateDialog(savedInstanceState)
        }
    }

    //DatePickerDialogから選択結果を受け取る
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        listener.selectedDate(year, month, dayOfMonth)
    }
}