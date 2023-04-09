package com.example.intellilearnteacherapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.content.ContextCompat
import com.example.intellilearnteacherapp.models.ClassModel
import kotlinx.android.synthetic.main.activity_my_attendance.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import storage.SharedPrefManager

class MyAttendance : AppCompatActivity() {

    private lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_attendance)

        spinner = findViewById(R.id.dropdown_attendance_type)

        //first get all the classes taught by the teacher
        //set the values for the spinner after fetching data
        val classes: ArrayList<String> = ArrayList()

        MyApp.getInstance().getApiServices().getTeacherClasses(SharedPrefManager.getInstance(applicationContext).teacher.teacher_ID).enqueue(object :
            Callback<List<ClassModel>> {

            override fun onResponse(call: Call<List<ClassModel>>, response: Response<List<ClassModel>>) {

                if (response.isSuccessful && !response.body().isNullOrEmpty()){

                    //now extract data
                    val classesList: List<ClassModel>? = response.body()

                    if (classesList != null) {
                        for (classDesc in classesList) {

                            classes.add("Class ".plus(classDesc.class_level).plus(" Section ").plus(classDesc.section).plus(" ").plus(classDesc.subject))

                        }
                    }

                    //overall attendance
                    classes.add("Overall")

                    // Update spinner on the main thread
                    //withContext(Dispatchers.Main) {
                    updateSpinner(classes)
                    //}

                }
                else{

                    Toast.makeText(this@MyAttendance, "Error loading Classes for Teacher", Toast.LENGTH_LONG).show()

                }

            }

            override fun onFailure(call: Call<List<ClassModel>>, t: Throwable) {

                Toast.makeText(this@MyAttendance, "Error loading Classes data", Toast.LENGTH_LONG).show()

            }


        })

        btnSubmit.setOnClickListener{

            val intent = Intent(this@MyAttendance, ViewAttendance::class.java)
            intent.putExtra("selectedClass", dropdown_attendance_type.selectedItem.toString())

            startActivity(intent)

        }

    }

    private fun updateSpinner(optionsList: List<String>) {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            optionsList
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

}