package com.example.y.tasknotebook

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.fragment_task.*


class TaskFragment : Fragment() {


    //Realmのインスタンスを取得
    var realm: Realm = Realm.getDefaultInstance()

    //RecyclerViewの列数を格納する変数
    private var spanCount: Int = 2

    //isPinnedがtrueのレコードを全取得
    private val pinnedResults: RealmResults<Task> = realm.where<Task>()
        .equalTo("isAchieved", false)
        .and()
        .equalTo("isPinned", true)
        .findAll()
        .sort("id", Sort.DESCENDING)

    //isPinnedがfalseのレコードを全取得
    private val notPinnedResults: RealmResults<Task> = realm.where<Task>()
        .equalTo("isAchieved", false)
        .and()
        .equalTo("isPinned", false)
        .findAll()
        .sort("id", Sort.DESCENDING)


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

        //画面の幅に応じた列数でRecyclerViewを表示
        parentLayout01.afterMeasured {
            spanCount = when(parentLayout01.width){
                in 0..599 -> 1
                in 600..1199 -> 2
                in 1200..1799 -> 3
                else -> 4
            }
            setRecyclerViewLayoutManager()
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
            .sort("id", Sort.DESCENDING)

        //searchRecyclerView01のadapterをセット
        searchRecyclerView01.adapter = FrameRecyclerViewAdapter(searchedResults)

        //searchRecyclerView01のレイアウトを設定
        setRecyclerViewLayoutManager()

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

        //pinRecyclerViewのadapterをセット
        pinRecyclerView.adapter = FrameRecyclerViewAdapter(pinnedResults)

        //もしピン止めされたレコードが無いなら、pinRecyclerViewを表示しない
        if(pinnedResults.size == 0){
            pinRecyclerView.visibility = View.GONE
        }else{
            pinRecyclerView.visibility = View.VISIBLE
        }

        //mainRecyclerViewのadapterをセット
        mainRecyclerView.adapter = FrameRecyclerViewAdapter(notPinnedResults)

        //未達成のタスクが0件なら、画面にメッセージを表示
        if(pinnedResults.size == 0 && notPinnedResults.size == 0){
            noTaskText01.visibility = View.VISIBLE
        }else{
            noTaskText01.visibility = View.INVISIBLE
        }

        //2つのRecyclerViewのLayoutManagerを設定
        setRecyclerViewLayoutManager()
    }


    //画面のレイアウトが行われるたびに、RecyclerViewの列数を更新
    private fun setRecyclerViewLayoutManager(){
        mainRecyclerView.layoutManager = GridLayoutManager(this.context, spanCount)
        searchRecyclerView01.layoutManager = GridLayoutManager(this.context, spanCount)
        pinRecyclerView.layoutManager = GridLayoutManager(this.context, spanCount)
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