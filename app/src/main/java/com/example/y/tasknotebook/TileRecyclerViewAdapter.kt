package com.example.y.tasknotebook

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.one_tile.view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class TileRecyclerViewAdapter(
    private val days: Array<LocalDate?>
): RecyclerView.Adapter<TileRecyclerViewAdapter.CustomViewHolder>() {



    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val dayText: TextView = itemView.dayText
        val tileLayout: ConstraintLayout = itemView.tileLayout
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.one_tile, parent, false)
        return CustomViewHolder(view)
    }


    override fun getItemCount(): Int {
        return days.size //days.sizeは35or42
    }


    @SuppressLint("SimpleDateFormat")
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

        //もし当日がnullなら、tileを非表示
        if(days[position] == null){
            holder.itemView.tileLayout.visibility = View.INVISIBLE
        }

        //当日がnullでない時だけ、tileの色の処理を行う
        if(days[position] != null){

            //当日開始日時 例: Tue Jun 29 00:00:00 GMT+09:00 2021
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val startDatetime: Date = formatter.parse(days[position].toString())!!

            //当日終了日時 例: Tue Jun 29 23:59:59 GMT+09:00 2021
            val endDatetime:Date = formatter.parse(days[position].toString())!!
            endDatetime.hours = 23
            endDatetime.minutes = 59
            endDatetime.seconds = 59

            //この後レコード検索を行うので、Realmのインスタンスを取得
            val realm = Realm.getDefaultInstance()

            //当日のタスクの達成数を取得
            val realmResults = realm.where<Task>()
                .between("achievedDate", startDatetime, endDatetime)
                .findAll()

            //当日のタスク達成数に応じて、tileの背景色を変更
            when(realmResults.size){
                0 -> holder.itemView.tileLayout.setBackgroundResource(R.drawable.background_tile_00)
                1 -> holder.itemView.tileLayout.setBackgroundResource(R.drawable.background_tile_01)
                2 -> holder.itemView.tileLayout.setBackgroundResource(R.drawable.background_tile_02)
                3 -> holder.itemView.tileLayout.setBackgroundResource(R.drawable.background_tile_03)
                4 -> holder.itemView.tileLayout.setBackgroundResource(R.drawable.background_tile_04)
                else -> holder.itemView.tileLayout.setBackgroundResource(R.drawable.background_tile_05)
            }

            //当日のタスク達成数が1件以上なら、dayTextを非表示
            if(realmResults.size != 0){
                holder.itemView.dayText.visibility = View.INVISIBLE
            }

            //タイルを押すと、OptionalSearchActivityへ遷移
            holder.itemView.tileLayout.setOnClickListener {
                val intent = Intent(it.context, OptionalSearchActivity::class.java)
                intent.putExtra("year", days[position]?.year)
                intent.putExtra("month", days[position]?.monthValue)
                intent.putExtra("day", days[position]?.dayOfMonth)
                it.context.startActivity(intent)
            }

        }

    }


}