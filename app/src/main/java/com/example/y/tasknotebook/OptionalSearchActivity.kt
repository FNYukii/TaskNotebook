package com.example.y.tasknotebook

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.GridLayoutManager
import io.realm.Realm
import io.realm.Sort
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_optional_search.*
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

class OptionalSearchActivity : AppCompatActivity() {


    //RecyclerViewの列数を格納する変数
    private var spanCount: Int = 2

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

        //画面の幅に応じた列数でRecyclerViewを表示
        parentLayout03.afterMeasured {
            spanCount = when(parentLayout03.width){
                in 0..599 -> 1
                in 600..1199 -> 2
                in 1200..1799 -> 3
                else -> 4
            }
            setRecyclerViewLayoutManager()
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
        optionalSearchRecyclerView.adapter = FrameRecyclerViewAdapter(realmResults)

        //optionalSearchRecyclerViewのレイアウトを設定
        setRecyclerViewLayoutManager()

        //realmResultsが0件なら、画面にメッセージを表示
        if(realmResults.size == 0){
            messageText03.visibility = View.VISIBLE
        }else{
            messageText03.visibility = View.INVISIBLE
        }

    }


    private fun setRecyclerViewLayoutManager(){
        optionalSearchRecyclerView.layoutManager = GridLayoutManager(this, spanCount)
    }


    //Viewのレイアウト完了時に処理を行うための拡張関数
    private inline fun <T : View> T.afterMeasured(crossinline f: T.() -> Unit) {
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (measuredWidth > 0 && measuredHeight > 0) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    f()
                }
            }
        })
    }


}