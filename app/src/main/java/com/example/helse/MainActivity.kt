package com.example.helse

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        open_airquality.setOnClickListener {
            startActivity(Intent(this, AirqualityActivity::class.java))
        }

        open_searchfrag.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .add(R.id.root_layout, SearchFragment())
                .commit()
        }

    }
}
