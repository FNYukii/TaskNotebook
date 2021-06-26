package com.example.y.tasknotebook

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import io.realm.Realm
import io.realm.Sort
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_optional_search.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

class OptionalSearchActivity : AppCompatActivity() {


    //日付用の変数
    private var year = 0
    private var month = 0
    private var day = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_optional_search)

        //intentから日付情報を受け取る
        year = intent.getIntExtra("year", 0)
        month = intent.getIntExtra("month", 0)
        day = intent.getIntExtra("day", 0)

        //dateLabelTextにテキストをセット
        val dateString = year.toString() + "年 " + month.toString() + "月 " + day.toString() + "日"
        dateLabelText.text = dateString

        //backButton03を押すと、finish
        backButton03.setOnClickListener {
            finish()
        }

    }


    @SuppressLint("SimpleDateFormat")
    override fun onStart() {
        super.onStart()

        //当日の開始日時と終了日時をLocalDatetime型変数に取得
        val startLocalDatetime: LocalDateTime = LocalDateTime.of(year, month, day, 0, 0, 0)
        val endLocalDatetime: LocalDateTime = LocalDateTime.of(year, month, day, 23, 59, 59)

        //Realmで検索するので、Date型の変数へ変換
        val startDate: Date = Date.from(ZonedDateTime.of(startLocalDatetime, ZoneId.systemDefault()).toInstant())
        val endDate: Date = Date.from(ZonedDateTime.of(endLocalDatetime, ZoneId.systemDefault()).toInstant())

        //Realmのインスタンス取得
        val realm = Realm.getDefaultInstance()

        //当日達成したタスクを全取得
        val realmResults = realm.where<Task>()
            .between("achievedDate", startDate, endDate)
            .findAll()
            .sort("achievedDate", Sort.DESCENDING)

        //optionalSearchRecyclerViewの処理
        optionalSearchRecyclerView.layoutManager = GridLayoutManager(this, 2)
        optionalSearchRecyclerView.adapter = FrameRecyclerViewAdapter(realmResults)

        //realmResultsが0件なら、画面にメッセージを表示
        if(realmResults.size == 0){
            messageText03.visibility = View.VISIBLE
        }else{
            messageText03.visibility = View.INVISIBLE
        }

    }


}