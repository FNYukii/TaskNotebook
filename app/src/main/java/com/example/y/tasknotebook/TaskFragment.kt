package com.example.y.tasknotebook

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.fragment_task.*


class TaskFragment : Fragment() {

    //Realmのインスタンスを取得
    var realm: Realm = Realm.getDefaultInstance()

    //全てのレコードを取得
    private val realmResults: RealmResults<Task> = realm.where<Task>().findAll()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //EditActivityへ遷移
        floatingButton.setOnClickListener {
            val intent = Intent(this.context, EditActivity::class.java)
            startActivity(intent)
        }

        //まだpinRecyclerView使わないので非表示
        pinRecyclerView.visibility = View.GONE
    }


    override fun onStart() {
        super.onStart()

        //mainRecyclerViewを表示
        mainRecyclerView.layoutManager = GridLayoutManager(this.context, 2)
        mainRecyclerView.adapter = FrameRecyclerViewAdapter(realmResults)
    }


}