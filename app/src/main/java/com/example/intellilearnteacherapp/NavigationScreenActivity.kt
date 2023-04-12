package com.example.intellilearnteacherapp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.intellilearnteacherapp.models.TeacherScheduleItem
import kotlinx.android.synthetic.main.activity_navigation_screen.*
import storage.SharedPrefManager
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class NavigationScreenActivity : AppCompatActivity() {

    val handler = Handler()
    val delay = 10 * 60 * 1000 // 10 minutes in milliseconds

    val runnable = object : Runnable {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun run() {

            val teacherID = SharedPrefManager.getInstance(this@NavigationScreenActivity).teacher.teacher_ID
            // Call your function or activity here
            // Call checkCurrentTimeAndPerformAction function
            checkCurrentTimeAndPingAttendance(this@NavigationScreenActivity, teacherID)

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
        btnMyNotes.setOnClickListener{

            val intent = Intent(this, MyNotes::class.java)
            startActivity(intent)

        }

        btnShowAnnouncements.setOnClickListener{

            val intent = Intent(this, MyAnnouncements::class.java)
            startActivity(intent)


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

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun checkCurrentTimeAndPingAttendance(context: Context, teacherID: Int) {
        val scheduleList: List<TeacherScheduleItem>? = getTeacherSchedule(context, teacherID)
        if (scheduleList != null) {
            val currentTime = LocalTime.now()
            for (item in scheduleList) {
                val startTime = LocalTime.parse(item.startTime, DateTimeFormatter.ofPattern("HH:mm:ss"))
                val endTime = startTime.plusMinutes(item.durationMinutes.toLong())

                Toast.makeText(this, "Ping", Toast.LENGTH_SHORT).show()

                if (currentTime.isAfter(startTime) && currentTime.isBefore(endTime)) {
                    // Perform the action here
                    // ping the server with attendance ping
                    Toast.makeText(this, "Ping", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}