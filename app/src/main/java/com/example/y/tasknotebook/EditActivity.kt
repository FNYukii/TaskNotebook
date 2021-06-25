package com.example.y.tasknotebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_edit.*
import java.time.LocalDateTime

class EditActivity : AppCompatActivity(), DeleteDialogFragment.DialogListener, AchieveDialogFragment.DialogListener {

    //Realmのインスタンス取得
    private var realm: Realm = Realm.getDefaultInstance()

    //変数
    private var id = 0
    private var isAchieved = false
    private var isPinned = false
    private var achievedYear = 0
    private var achievedMonth = 0
    private var achievedDay = 0
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
            achievedDay = task.achievedDay
            achievedHour = task.achievedHour
            achievedMinute = task.achievedMinute
            setPinIcon()
        }


        //backButtonを押すとfinish
        backButton.setOnClickListener {
            saveRecord()
            finish()
        }

        //achieveButtonを押すと、AchieveDialogを呼び出す
        achieveButton.setOnClickListener {
            val dialogFragment = AchieveDialogFragment()
            val args = Bundle()
            args.putBoolean("isAchieved", isAchieved)
            dialogFragment.arguments = args
            dialogFragment.show(supportFragmentManager, "dialog")
        }

        //pinButtonを押すと、ピン止めを切り替える
        pinButton.setOnClickListener {
            isPinned =!isPinned
            setPinIcon()
        }

        //deleteButtonを押すと、DeleteDialogを呼び出す
        deleteButton.setOnClickListener {
            val dialogFragment = DeleteDialogFragment()
            dialogFragment.show(supportFragmentManager, "dialog")
        }

    }


    override fun onBackPressed() {
        super.onBackPressed()
        saveRecord()
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
            task.achievedDay = achievedDay
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
            task?.achievedDay = achievedDay
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

    override fun onDialogDeleteReceive(dialog: DialogFragment) {
        isGarbage = true
        saveRecord()
        finish()
    }

    override fun onDialogAchieveReceive(dialog: DialogFragment) {
        if(!isAchieved){
            //タスクを達成済みにする
            isAchieved = true
            //現在日時を各フィールドに保存
            val now = LocalDateTime.now()
            achievedYear = now.year
            achievedMonth = now.monthValue
            achievedDay = now.dayOfMonth
            achievedHour = now.hour
            achievedMinute = now.minute
            Log.d("hello", "Year: $achievedYear")
            Log.d("hello", "Month: $achievedMonth")
            Log.d("hello", "Date: $achievedDay")
            Log.d("hello", "Hour: $achievedHour")
            Log.d("hello", "Minute: $achievedMinute")
        }else{
            //未達成にする
            isAchieved = false
            achievedYear = -1
            achievedMonth = -1
            achievedDay = -1
            achievedHour = -1
            achievedMinute = -1
        }
        saveRecord()
        finish()
    }


}
