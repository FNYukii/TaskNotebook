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
    private var isGarbage = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        //Intentにidがあれば取得
        id = intent.getIntExtra("id", 0)

        //もしIntentからタスクのidを受け取ったら、そのタスクを編集する
        if(id != 0){
            val task = realm.where<Task>()
                .equalTo("id", id)
                .findFirst()
            isAchieved = task?.isAchieved!!
            isPinned = task.isPinned
            titleEdit.setText(task.title)
            detailEdit.setText(task.detail)
            achievedYear = task.achievedYear
            achievedMonth = task.achievedMonth
            achievedDate = task.achievedDate
            achievedHour = task.achievedHour
            achievedMinute = task.achievedMinute
            setPinIcon()
        }


        //backButtonを押すとfinish
        backButton.setOnClickListener {
            saveRecord()
            finish()
        }

        //pinButtonを押すと、ピン止めを切り替える
        pinButton.setOnClickListener {
            isPinned =!isPinned
            setPinIcon()
        }

        //deleteButtonを押すとタスクを削除
        deleteButton.setOnClickListener {
            isGarbage = true
            saveRecord()
            finish()
        }

    }


    //pinButtonのアイコンを切り替える
    private fun setPinIcon(){
        if(isPinned){
            pinButton.setImageResource(R.drawable.ic_baseline_push_pin_24)
        }else{
            pinButton.setImageResource(R.drawable.ic_outline_push_pin_24)
        }
    }


    private fun saveRecord(){
        //タイトルか説明のどちらかが埋められているなら、新規レコードを追加、もしくは編集対象のレコードを更新
        if(!isGarbage && (titleEdit.text.isNotEmpty() || detailEdit.text.isNotEmpty())){
            if(id == 0){
                insertRecord()
            }else{
                updateRecord()
            }
        }

        //タイトルと説明どちらもempty、またはisGarbageがtrueなら、編集対象のレコードを削除
        if(isGarbage || titleEdit.text.isEmpty() && detailEdit.text.isEmpty()){
            if(id != 0){
                deleteRecord()
            }
        }
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


    private fun updateRecord() {
        //編集対象のレコードを取得
        val task = realm.where<Task>()
            .equalTo("id", id)
            .findFirst()

        //レコードの各フィールドに値を格納
        realm.executeTransaction {
            task?.isAchieved = isAchieved
            task?.isPinned = isPinned
            task?.title = titleEdit.text.toString()
            task?.detail = detailEdit.text.toString()
            task?.achievedYear = achievedYear
            task?.achievedMonth = achievedMonth
            task?.achievedDate = achievedDate
            task?.achievedHour = achievedHour
            task?.achievedMinute = achievedMinute
        }
    }


    private fun deleteRecord() {
        //該当のレコードを取得
        val achievement = realm.where<Task>()
            .equalTo("id", id)
            .findFirst()

        //レコード削除
        realm.executeTransaction {
            achievement?.deleteFromRealm()
        }
    }


}
