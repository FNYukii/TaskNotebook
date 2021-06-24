package com.example.y.tasknotebook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.*
import kotlinx.android.synthetic.main.one_frame.view.*


class FrameRecyclerViewAdapter(
    private var collection: OrderedRealmCollection<Task>?
): RealmRecyclerViewAdapter<Task, FrameRecyclerViewAdapter.CustomViewHolder>(collection, true) {

    class CustomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val frameTitleText: TextView = itemView.frameTitleText
        val frameDetailText: TextView = itemView.frameDetailText
    }

}