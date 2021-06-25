package com.example.y.tasknotebook

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import java.lang.Exception

class DeleteDialogFragment : DialogFragment() {


    //EditActivityへ削除命令を渡すためのインターフェース
    interface DialogListener{
        fun onDialogDeleteReceive(dialog: DialogFragment)
    }
    private var listener:DialogListener? = null


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it, R.style.CustomDialog)
            builder.setTitle("確認")
                .setMessage("このタスクを削除しますか?")
                .setPositiveButton("OK"
                ) { _, _ ->
                    //EditActivityへ削除命令を送る
                    listener?.onDialogDeleteReceive(this)
                }
                .setNegativeButton("キャンセル"
                ) { _, _ ->
                    //do nothing
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as DialogListener
        }catch (e: Exception){
            Toast.makeText(this.context, "Error! Can not find listener", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDetach() {
        super.onDetach()
        listener = null
    }


}