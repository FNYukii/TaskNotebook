package com.example.y.tasknotebook

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.fragment_achieved_task.*
import kotlinx.android.synthetic.main.fragment_task.*


class AchievedTaskFragment : Fragment() {


    //Realmのインスタンスを取得
    var realm: Realm = Realm.getDefaultInstance()

    //isPinnedがtrueのレコードを全取得
    private val achievedResults: RealmResults<Task> = realm.where<Task>()
        .equalTo("isAchieved", true)
        .findAll()
        .sort("achievedDate", Sort.DESCENDING)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_achieved_task, container, false)
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //検索バーのqueryが変化するたびに検索する
        searchView02.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                val query = newText!!
                search(query)
                return false
            }
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView01.clearFocus()
                return false
            }
        })

        //検索バー以外の領域をタップすると、検索バーからフォーカスを外す！
        screenCover02.setOnTouchListener { _, _ ->
            searchView02.clearFocus()
            false
        }

    }

    private fun search(query: String) {

        //queryを含む達成済みのタスクをあいまい検索して取得
        val searchedResults: RealmResults<Task> = realm.where<Task>()
            .contains("title", query)
            .and()
            .equalTo("isAchieved", true)
            .or()
            .contains("detail", query)
            .and()
            .equalTo("isAchieved", true)
            .findAll()

        //searchRecyclerView02の処理
        searchRecyclerView02.layoutManager = GridLayoutManager(this.context, 2)
        searchRecyclerView02.adapter = FrameRecyclerViewAdapter(searchedResults)

        //検索結果が0件なら、画面にメッセージを表示
        if(searchedResults.size == 0){
            notSearchedText02.visibility = View.VISIBLE
        }else{
            notSearchedText02.visibility = View.INVISIBLE
        }

        //queryがemptyなら、searchRecyclerViewは表示しない。
        if(query.isEmpty()){
            searchRecyclerView02.visibility = View.GONE
            achievedRecyclerView.visibility = View.VISIBLE
            notSearchedText02.visibility = View.INVISIBLE
        }else{
            searchRecyclerView02.visibility = View.VISIBLE
            achievedRecyclerView.visibility = View.GONE
        }

    }


    override fun onStart() {
        super.onStart()

        //achievedRecyclerViewの処理
        achievedRecyclerView.layoutManager = GridLayoutManager(this.context, 2)
        achievedRecyclerView.adapter = FrameRecyclerViewAdapter(achievedResults)

        //達成済みのタスクが0件なら、画面にメッセージを表示
        if(achievedResults.size == 0){
            noTaskText02.visibility = View.VISIBLE
        }else {
            noTaskText02.visibility = View.GONE
        }

    }


}