package com.example.y.tasknotebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.realm.Realm
import io.realm.RealmObject
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_edit.*

class EditActivity : AppCompatActivity() {

    //Realmのインスタンス取得
    private var realm: Realm = Realm.getDefaultInstance()

    //変数
    private var id = 0
    private var isAchieved = false
    private var isPinned = false
    private var achievedYear = 0
    private var achievedMonth = 0
    private var achievedDate = 0
    private var achievedHour = 0
    private var achievedMinute = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        //backButtonを押すとfinish
        backButton.setOnClickListener {
            saveRecord()
            finish()
        }

    }


    private fun saveRecord(){
        insertRecord()
    }

    private fun insertRecord() {

        //新規レコード用のidを生成する
        var maxId = realm.where<Task>().max("id")?.toInt()
        if(maxId == null) maxId = 0
        val newId = maxId + 1

        //新規レコード追加
        realm.executeTransaction {
            val task = realm.createObject<Task>(newId)
            task.isAchieved = isAchieved
            task.isPinned = isPinned
            task.title = titleEdit.text.toString()
            task.detail = detailEdit.text.toString()
            task.achievedYear = achievedYear
            task.achievedMonth = achievedMonth
            task.achievedDate = achievedDate
            task.achievedHour = achievedHour
            task.achievedMinute = achievedMinute
        }
    }

}