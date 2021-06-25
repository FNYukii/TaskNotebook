package com.example.y.tasknotebook

import android.graphics.Color
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.one_tile.view.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TileRecyclerViewAdapter(
    private val days: Array<LocalDate?>
): RecyclerView.Adapter<TileRecyclerViewAdapter.CustomViewHolder>() {



    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val dayText: TextView = itemView.dayText
        val tileImage: ConstraintLayout = itemView.tileLayout
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.one_tile, parent, false)
        return CustomViewHolder(view)
    }


    override fun getItemCount(): Int {
        return days.size //days.sizeは35or42
    }


    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {


        //TextViewに値をセット
        if(days[position] == null){
            holder.dayText.text = ""
        }else{
            val formatter = DateTimeFormatter.ofPattern("d")
            holder.dayText.text = days[position]?.format(formatter)
        }

        //もし本日なら、dayTextを強い色で表示
        if(days[position] == LocalDate.now()){
            holder.dayText.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.strong))
        }



    }



}