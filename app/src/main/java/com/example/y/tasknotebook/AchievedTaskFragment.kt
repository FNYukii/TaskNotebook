package com.example.y.tasknotebook

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.fragment_achieved_task.*


class AchievedTaskFragment : Fragment() {


    //Realmのインスタンスを取得
    var realm: Realm = Realm.getDefaultInstance()

    //isPinnedがtrueのレコードを全取得
    private val achievedResults: RealmResults<Task> = realm.where<Task>()
        .equalTo("isAchieved", true)
        .findAll()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_achieved_task, container, false)
    }


    override fun onStart() {
        super.onStart()

        //achievedRecyclerViewの処理
        achievedRecyclerView.layoutManager = GridLayoutManager(this.context, 2)
        achievedRecyclerView.adapter = FrameRecyclerViewAdapter(achievedResults)
    }


}