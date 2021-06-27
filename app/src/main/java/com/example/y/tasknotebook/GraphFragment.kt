package com.example.y.tasknotebook

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlinx.android.synthetic.main.fragment_graph.*


class GraphFragment : Fragment() {


    //カレンダーのページ数
    private val pageSize = Int.MAX_VALUE


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_graph, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //CalendarPagerを表示
        graphPager.adapter = CustomPagerAdapter(this.context as FragmentActivity)
        graphPager.setCurrentItem(pageSize / 2, false)
    }


    private inner class CustomPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int {
            return pageSize
        }
        override fun createFragment(position: Int): Fragment {
            val fragment = PagerFragment()
            val bundle = Bundle()
            bundle.putInt("position", position)
            fragment.arguments = bundle
            return fragment
        }
    }

}