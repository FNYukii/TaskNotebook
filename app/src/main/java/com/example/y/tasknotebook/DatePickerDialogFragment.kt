package com.example.y.tasknotebook

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.widget.DatePicker
import androidx.core.content.ContextCompat
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
        val context = context
        if(context != null) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = Date().time
            calendar.timeInMillis = Date().time
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(context, this, year, month, day)

            return datePickerDialog
        }else{
            return super.onCreateDialog(savedInstanceState)
        }
    }

    //DatePickerDialogから選択結果を受け取る
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        listener.selectedDate(year, month, dayOfMonth)
    }
}