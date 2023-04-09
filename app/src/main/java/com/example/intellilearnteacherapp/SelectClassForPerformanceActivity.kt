package com.example.intellilearnteacherapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.intellilearnteacherapp.models.ClassModel
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import storage.SharedPrefManager

//an activity to let the teacher choose the class for checking the performance
class SelectClassForPerformanceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_class_for_performance)

        val buttonContainer = findViewById<LinearLayout>(R.id.button_container)

        MyApp.getInstance().getApiServices().getTeacherClasses(SharedPrefManager.getInstance(applicationContext).teacher.teacher_ID).enqueue(object :
            Callback<List<ClassModel>> {

            override fun onResponse(call: Call<List<ClassModel>>, response: Response<List<ClassModel>>) {

                if (response.isSuccessful && !response.body().isNullOrEmpty()){

                    //now extract data
                    val classesList: List<ClassModel>? = response.body()
                    val classesDescs: ArrayList<String> = ArrayList()

                    if (classesList != null) {
                        for (classDesc in classesList) {

                            classesDescs.add("Class ".plus(classDesc.class_level).plus(" Section ").plus(classDesc.section).plus(" ").plus(classDesc.subject))

                        }
                    }


                    for (className in classesDescs) {

                        val button = Button(this@SelectClassForPerformanceActivity).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            ).apply {
                                setMargins(0, 0, 0, 16)
                            }
                            backgroundTintList = ContextCompat.getColorStateList(
                                this@SelectClassForPerformanceActivity,
                                R.color.button_background_color
                            )
                            text = className
                            setTextColor(
                                ContextCompat.getColor(
                                    this@SelectClassForPerformanceActivity,
                                    android.R.color.white
                                )
                            )
                            textSize = 18f
                        }

                        button.setOnClickListener {
                            // Handle the button click event

                            val intent = Intent(this@SelectClassForPerformanceActivity, selectReportType::class.java)
                            intent.putExtra("buttonText", button.text)
                            startActivity(intent)

                        }

                        buttonContainer.addView(button)
                    }

                }
                else{

                    Toast.makeText(this@SelectClassForPerformanceActivity, "Error loading Classes for Teacher", Toast.LENGTH_LONG).show()

                }

            }

            override fun onFailure(call: Call<List<ClassModel>>, t: Throwable) {

                Toast.makeText(this@SelectClassForPerformanceActivity, "Error loading Classes data", Toast.LENGTH_LONG).show()

            }


        })


    }
}
