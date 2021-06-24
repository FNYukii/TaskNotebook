package com.example.y.tasknotebook

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.*
import kotlinx.android.synthetic.main.one_frame.view.*


class FrameRecyclerViewAdapter(private val realmResults: RealmResults<Task>): RecyclerView.Adapter<FrameRecyclerViewAdapter.CustomViewHolder>(){


    class CustomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val frameTitleText: TextView = itemView.frameTitleText
        val frameDetailText: TextView = itemView.frameDetailText
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.one_frame, parent, false)
        return CustomViewHolder(view)
    }


    override fun getItemCount(): Int {
        return realmResults.size
    }


    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        //レコードを取得
        val task = realmResults[position]

        //タイトルと説明をTextViewへセット
        holder.frameTitleText.text = task?.title.toString()
        holder.frameDetailText.text = task?.detail.toString()

        //EditActivityへ遷移するクリックリスナーをセット
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, EditActivity::class.java)
            intent.putExtra("id", task?.id)
            it.context.startActivity(intent)
        }
    }


}