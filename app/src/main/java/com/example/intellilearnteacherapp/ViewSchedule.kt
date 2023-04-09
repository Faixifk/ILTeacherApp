package com.example.intellilearnteacherapp

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.intellilearnteacherapp.models.TeacherScheduleItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import storage.SharedPrefManager

// Helper function to save schedule data to SharedPreferences
fun saveTeacherSchedule(context: Context, teacherID: Int, scheduleList: List<TeacherScheduleItem>) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("teacher_schedule", Context.MODE_PRIVATE)
    val editor: SharedPreferences.Editor = sharedPreferences.edit()
    val gson = Gson()
    val json = gson.toJson(scheduleList)
    editor.putString("teacher_$teacherID", json)
    editor.apply()
}

// Helper function to retrieve schedule data from SharedPreferences
fun getTeacherSchedule(context: Context, teacherID: Int): List<TeacherScheduleItem>? {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("teacher_schedule", Context.MODE_PRIVATE)
    val gson = Gson()
    val json: String? = sharedPreferences.getString("teacher_$teacherID", null)
    return if (json == null) {
        null
    } else {
        val type = object : TypeToken<List<TeacherScheduleItem>>() {}.type
        gson.fromJson(json, type)
    }
}


class ViewSchedule : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_schedule)

        val teacherID = SharedPrefManager.getInstance(this@ViewSchedule).teacher.teacher_ID


        // Set up the refresh FloatingActionButton
        findViewById<FloatingActionButton>(R.id.refresh_fab).setOnClickListener {
            refreshSchedule(teacherID)
        }

        // Try to retrieve the schedule data from SharedPreferences
        var scheduleList: List<TeacherScheduleItem>? = getTeacherSchedule(this@ViewSchedule, teacherID)

        if (scheduleList == null) {

            // If the schedule data is not found in SharedPreferences, fetch it from the API
            fetchScheduleFromApi(teacherID)

        } else {
            // If the schedule data is found in SharedPreferences, update the UI
            setupRecyclerView(scheduleList!!)
        }
    }

    private fun setupRecyclerView(scheduleList: List<TeacherScheduleItem>) {
        //create the table
        val recyclerView = findViewById<RecyclerView>(R.id.schedule_table)
        val adapter = TeacherScheduleAdapter(scheduleList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this@ViewSchedule)
    }

    private fun refreshSchedule(teacherID: Int) {

        fetchScheduleFromApi(teacherID)
        Toast.makeText(this@ViewSchedule, "Schedule updated!", Toast.LENGTH_SHORT).show()
    }

    private fun fetchScheduleFromApi(teacherID: Int) {
        MyApp.getInstance().getApiServices().getTeacherSchedule(teacherID)
            .enqueue(object : Callback<List<TeacherScheduleItem>> {
                override fun onResponse(
                    call: Call<List<TeacherScheduleItem>>,
                    response: Response<List<TeacherScheduleItem>>
                ) {
                    if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                        val scheduleList: List<TeacherScheduleItem>? = response.body()
                        Log.e("resp", response.body().toString())

                        // Save the fetched schedule data to SharedPreferences
                        saveTeacherSchedule(this@ViewSchedule, teacherID, scheduleList!!)

                        // Update the UI
                        setupRecyclerView(scheduleList!!)
                    } else {
                        Toast.makeText(applicationContext, "No schedule data!", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<List<TeacherScheduleItem>>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }
            })
    }


}
