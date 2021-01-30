package com.udacity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val statusExtra = intent.getStringExtra("status")
        val fileName = intent.getStringExtra("filename")

        filename.text = fileName
        status.text = statusExtra

        if (statusExtra == "FAIL") {
            status.setTextColor(Color.RED)
        } else if (statusExtra == "SUCCESS") {
            status.setTextColor(Color.GREEN)
        }

    }


    fun goBack(view: View) {
        onBackPressed()
    }
}
