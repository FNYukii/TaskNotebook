package com.example.y.tasknotebook

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.DialogFragment
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_edit.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class EditActivity :
    AppCompatActivity(),
    DeleteDialogFragment.DialogListener,
    AchieveDialogFragment.DialogListener,
    DatePickerDialogFragment.OnSelectedDateListener,
    TimePickerDialogFragment.OnSelectedTimeListener
{

    //Realmのインスタンス取得
    private var realm: Realm = Realm.getDefaultInstance()

    //変数
    private var id = 0
    private var isAchieved = false
    private var isPinned = false
    private var achievedDate: Date? = null
    private var isGarbage = false


    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        //Intentにidがあれば取得
        id = intent.getIntExtra("id", 0)

        //もしIntentからタスクのidを受け取ったら、そのタスクを編集する
        if(id != 0){

            //編集対象のレコードを取得
            val task = realm.where<Task>()
                .equalTo("id", id)
                .findFirst()

            //各フィールドの値を変数へ格納したり、Viewへセットしたりする
            isAchieved = task?.isAchieved!!
            isPinned = task.isPinned
            titleEdit.setText(task.title)
            detailEdit.setText(task.detail)
            achievedDate = task.achievedDate
            setPinIcon()

            //もし達成済みのタスクなら達成日時を表示する
            if(isAchieved){

                //pinButtonは非表示&達成日時を表示
                pinButton.visibility = View.GONE

                //達成年日の文字列を生成
                val dateFormatter = SimpleDateFormat("yyyy年 M月 d日")
                val achievedDateString: String = dateFormatter.format(achievedDate!!)

                //達成曜日の文字列を生成
                val achievedLocalDate = achievedDate!!.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                val achievedDayOfWeek: Int =  achievedLocalDate.dayOfWeek.value
                var achievedDayOfWeekString: String = ""
                when(achievedDayOfWeek){
                    1 -> achievedDayOfWeekString = " (日)"
                    2 -> achievedDayOfWeekString = " (月)"
                    3 -> achievedDayOfWeekString = " (火)"
                    4 -> achievedDayOfWeekString = " (水)"
                    5 -> achievedDayOfWeekString = " (木)"
                    6 -> achievedDayOfWeekString = " (金)"
                    7 -> achievedDayOfWeekString = " (土)"
                }

                //達成日をTextViewへセット
                achievedDateText.text = (achievedDateString + achievedDayOfWeekString)

                //達成時刻をTextViewへセット
                val timeFormatter = SimpleDateFormat("HH:mm")
                achievedTimeText.text = timeFormatter.format(achievedDate!!)

            }else{
                achievedDatetimeContainer.visibility = View.GONE
            }
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

        //達成日が押されたら、ダイアログを呼ぶ
        achievedDateText.setOnClickListener {
            val datePickerDialogFragment = DatePickerDialogFragment()
            datePickerDialogFragment.show(supportFragmentManager, null)
        }

        //達成時刻が押されたら、ダイアログを呼ぶ
        achievedTimeText.setOnClickListener {
            val timePickerDialogFragment = TimePickerDialogFragment()
            timePickerDialogFragment.show(supportFragmentManager, null)
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
            task.achievedDate = achievedDate
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
            task?.achievedDate = achievedDate
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
            //タスクを達成済みにして、ピン止めを解除
            isAchieved = true
            isPinned = false
            //現在日時を各フィールドに保存
            achievedDate = Date()
        }else{
            //未達成にする
            isAchieved = false
            achievedDate = null
        }
        saveRecord()
        finish()
    }

    @SuppressLint("SimpleDateFormat")
    override fun selectedDate(year: Int, month: Int, date: Int) {


        //達成日を変更
        Log.d("hello", "year: $year")
//        achievedDate?.year = year  //なぜか正しい年を格納できない…
        Log.d("hello", "achievedDate.year: ${achievedDate?.year}")
        achievedDate?.month = month
        achievedDate?.date = date

        //達成日をTextViewへセット
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
        achievedDateText.text = dateFormatter.format(achievedDate!!)
    }

    @SuppressLint("SimpleDateFormat")
    override fun selectedTime(hour: Int, minute: Int) {

        //達成時刻を変更
        achievedDate?.hours = hour
        achievedDate?.minutes = minute

        //達成時刻をTextViewへセット
        val timeFormatter = SimpleDateFormat("HH:mm")
        achievedTimeText.text = timeFormatter.format(achievedDate!!)
    }


}
