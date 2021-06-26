package com.example.y.tasknotebook

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.*
import kotlinx.android.synthetic.main.one_frame.view.*
import java.text.SimpleDateFormat


class FrameRecyclerViewAdapter(
    private val realmResults: RealmResults<Task>,
    private val isOptionalSearch: Boolean
    ): RecyclerView.Adapter<FrameRecyclerViewAdapter.CustomViewHolder>(){


    class CustomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val framePinImage: ImageView = itemView.framePinImage
        val frameAchieveImage: ImageView = itemView.frameAchieveImage
        val frameAchievedDateText: TextView = itemView.frameAchievedDateText
        val frameAchievedTimeText: TextView = itemView.frameAchievedTimeText
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


    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        //レコードを取得
        val task = realmResults[position]

        //isPinnedの真偽に応じて、ピンアイコンの表示を切り替え
        if(task?.isPinned == false){
            holder.framePinImage.visibility = View.GONE
        }

        //達成年月日をTextViewへセット
        if(task?.achievedDate != null){
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val achievedDate: String = formatter.format(task.achievedDate!!).toString()
            holder.frameAchievedDateText.text = achievedDate
        }

        //達成時刻をTextViewへセット
        if(task?.achievedDate != null){
            val formatter = SimpleDateFormat("HH:mm")
            val achievedTime: String = formatter.format(task.achievedDate!!).toString()
            holder.frameAchievedTimeText.text = achievedTime
        }

        //isAchievedの真偽に応じて、達成アイコンと達成年月日・達成時刻の表示を切り替え
        if(task?.isAchieved == false){
            holder.frameAchievedDateText.visibility = View.GONE
            holder.frameAchievedTimeText.visibility = View.GONE
            holder.frameAchieveImage.visibility = View.GONE
        }

        //もしクラスの呼び出し元がOptionalSearchActivityなら、達成年月日を非表示
        if(isOptionalSearch){
            holder.frameAchievedDateText.visibility = View.GONE
        }

        //タイトルと説明をTextViewへセット
        holder.frameTitleText.text = task?.title.toString()
        holder.frameDetailText.text = task?.detail.toString()

        //もしtitleがemptyなら、titleTextを非表示にする
        if(task?.title.isNullOrEmpty()){
            holder.frameTitleText.visibility = View.GONE
        }else{
            holder.frameTitleText.visibility = View.VISIBLE
        }

        //EditActivityへ遷移するクリックリスナーをセット
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, EditActivity::class.java)
            intent.putExtra("id", task?.id)
            it.context.startActivity(intent)
        }
    }


}