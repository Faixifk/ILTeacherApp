package com.example.intellilearnteacherapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_select_report_type.*

class selectReportType : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_report_type)

        btnShowMinMaxAvg.setOnClickListener{

            val intent2 = Intent(this@selectReportType, showMinMaxAvg::class.java)
            intent2.putExtra("buttonText", intent.getStringExtra("buttonText"))
            startActivity(intent2)

        }

    }
}