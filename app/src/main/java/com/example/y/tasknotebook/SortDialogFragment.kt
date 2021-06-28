package com.example.y.tasknotebook

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import java.lang.Exception

class SortDialogFragment: DialogFragment() {


    //ホストActivityへ値を渡すインターフェース
    interface DialogListener{
        fun onDialogSortIdReceive(dialog: DialogFragment, sortId: Int)
    }
    private var listener:DialogListener? = null

    //配列や変数
    private val sortTypes = arrayOf("作成日時", "最終更新日時")
    private var sortId: Int = -1


    //dialogを生成
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it, R.style.CustomDialog)
            builder.setTitle("表示順を選択")
                .setSingleChoiceItems(sortTypes, -1){ _, which ->
                    sortId = which
                }
                .setPositiveButton("OK"
                ) { _, _ ->
                    if (sortId != -1) {
                        listener?.onDialogSortIdReceive(this, sortId)
                    }
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