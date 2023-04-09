package com.example.intellilearnteacherapp

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_navigation_screen.*
import storage.SharedPrefManager

class NavigationScreenActivity : AppCompatActivity() {

    val handler = Handler()
    val delay = 10 * 60 * 1000 // 10 minutes in milliseconds

    val runnable = object : Runnable {
        override fun run() {
            // Call your function or activity here
            // For example, if you want to start an activity:
            MyApp.getInstance().getApiServices()

            // Schedule the next call
            handler.postDelayed(this, delay.toLong())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_screen)

        // Call the runnable for the first time
        //this function is used to ping the server for teacher attendance

        handler.postDelayed(runnable, delay.toLong())


        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.colorAccent)
        }

        btnShowMcqList.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }

        btnLogout.setOnClickListener{

            SharedPrefManager.getInstance(applicationContext).clear()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }

        btnShowClassPerformance.setOnClickListener{

            val intent = Intent(this, SelectClassForPerformanceActivity::class.java)
            startActivity(intent)

        }

        btnSamplePlots.setOnClickListener{

            val intent = Intent(this, SampleBarPLot::class.java)
            startActivity(intent)

        }

        btnAskAI.setOnClickListener{

            val intent = Intent(this, AskAI::class.java)
            startActivity(intent)

        }

        btnMyAttendance.setOnClickListener{

            val intent = Intent(this, MyAttendance::class.java)
            startActivity(intent)

        }

        btnAskChatGPT.setOnClickListener{

            val intent = Intent(this, AskChatGPT::class.java)
            startActivity(intent)

        }

        btnMySchedule.setOnClickListener{

            val intent = Intent(this, ViewSchedule::class.java)
            startActivity(intent)

        }

        btnModelViewer.setOnClickListener{

            val i=packageManager.getLaunchIntentForPackage("org.andresoviedo.dddmodel2")
            if(i!=null)
            {
                startActivity(i)
            }else
            {
                Toast.makeText(this,"Success",Toast.LENGTH_LONG)

            }

        }

    }

    override fun onStart() {
        super.onStart()

        if(!SharedPrefManager.getInstance(this).isLoggedIn){

            val intent = Intent(applicationContext, LoginActivity::class.java, )
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)


        }

    }


}