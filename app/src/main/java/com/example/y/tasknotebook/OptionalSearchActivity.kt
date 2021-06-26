package com.example.y.tasknotebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_optional_search.*

class OptionalSearchActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_optional_search)

        //backButton03を押すと、finish
        backButton03.setOnClickListener {
            finish()
        }

    }


}