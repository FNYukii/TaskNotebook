package com.example.y.tasknotebook

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.fragment_task.*


class TaskFragment : Fragment(), SortDialogFragment.DialogListener {

    //Realmのインスタンスを取得
    var realm: Realm = Realm.getDefaultInstance()

    //isPinnedがtrueのレコードを全取得
    private val pinnedResults: RealmResults<Task> = realm.where<Task>()
        .equalTo("isAchieved", false)
        .and()
        .equalTo("isPinned", true)
        .findAll()

    //isPinnedがfalseのレコードを全取得
    private val notPinnedResults: RealmResults<Task> = realm.where<Task>()
        .equalTo("isAchieved", false)
        .and()
        .equalTo("isPinned", false)
        .findAll()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task, container, false)
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //floatingButtonを押すと、EditActivityへ遷移
        floatingButton.setOnClickListener {
            val intent = Intent(this.context, EditActivity::class.java)
            startActivity(intent)
        }

        //検索バーのqueryが変化するたびに検索する
        searchView01.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
        screenCover01.setOnTouchListener { _, _ ->
            searchView01.clearFocus()
            false
        }

        //sortButtonが押されたら、SortDialogを表示
        sortButton01.setOnClickListener {
            val dialogFragment = SortDialogFragment()
            dialogFragment.show(childFragmentManager, "dialog")
        }

    }

    private fun search(query: String) {

        //queryを含む未達成のタスクをあいまい検索して取得
        val searchedResults: RealmResults<Task> = realm.where<Task>()
            .contains("title", query)
            .and()
            .equalTo("isAchieved", false)
            .or()
            .contains("detail", query)
            .and()
            .equalTo("isAchieved", false)
            .findAll()

        //searchRecyclerView01の処理
        searchRecyclerView01.layoutManager = GridLayoutManager(this.context, 2)
        searchRecyclerView01.adapter = FrameRecyclerViewAdapter(searchedResults)

        //検索結果が0件なら、画面にメッセージを表示
        if(searchedResults.size == 0){
            notSearchedText01.visibility = View.VISIBLE
        }else{
            notSearchedText01.visibility = View.INVISIBLE
        }

        //queryがemptyなら、searchRecyclerViewは表示しない。
        if(query.isEmpty()){
            pinRecyclerView.visibility = View.VISIBLE
            mainRecyclerView.visibility = View.VISIBLE
            searchRecyclerView01.visibility = View.GONE
            notSearchedText01.visibility = View.INVISIBLE
        }else{
            pinRecyclerView.visibility = View.GONE
            mainRecyclerView.visibility = View.GONE
            searchRecyclerView01.visibility = View.VISIBLE
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

        //未達成のタスクが0件なら、画面にメッセージを表示
        if(pinnedResults.size == 0 && notPinnedResults.size == 0){
            noTaskText01.visibility = View.VISIBLE
        }else{
            noTaskText01.visibility = View.INVISIBLE
        }

    }

    override fun onDialogSortIdReceive(dialog: DialogFragment, sortId: Int) {
        Toast.makeText(this.context, "sortId: $sortId", Toast.LENGTH_SHORT).show()
    }


}