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

    //isPinnedがtrueのレコードを全取得
    private val pinnedResults: RealmResults<Task> = realm.where<Task>()
        .equalTo("isPinned", true)
        .findAll()

    //isPinnedがfalseのレコードを全取得
    private val notPinnedResults: RealmResults<Task> = realm.where<Task>()
        .equalTo("isPinned", false)
        .findAll()


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

    }


    override fun onStart() {
        super.onStart()

        //pinRecyclerViewの処理
        pinRecyclerView.layoutManager = GridLayoutManager(this.context, 2)
        pinRecyclerView.adapter = FrameRecyclerViewAdapter(pinnedResults)

        //もしピン止めされたレコードが無いなら、pinRecyclerViewを表示しない
        if(pinnedResults.size == 0){
            pinRecyclerView.visibility = View.GONE
        }else{
            pinRecyclerView.visibility = View.VISIBLE
        }

        //mainRecyclerViewの処理
        mainRecyclerView.layoutManager = GridLayoutManager(this.context, 2)
        mainRecyclerView.adapter = FrameRecyclerViewAdapter(notPinnedResults)
    }


}