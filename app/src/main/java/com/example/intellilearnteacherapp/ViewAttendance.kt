package com.example.intellilearnteacherapp

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.intellilearnteacherapp.models.LoginResponse
import com.example.intellilearnteacherapp.models.MarksModel
import com.example.intellilearnteacherapp.models.Teacher
import com.example.intellilearnteacherapp.models.TeacherAttendanceItem
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.activity_view_attendance.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import storage.SharedPrefManager

class ViewAttendance : AppCompatActivity() {

    lateinit var pieChart: PieChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_attendance)

        //Toast.makeText(this@ViewAttendance, intent.getStringExtra("selectedClass").toString(), Toast.LENGTH_LONG).show()

        val teacherID = SharedPrefManager.getInstance(this@ViewAttendance).teacher.teacher_ID
        val attendanceType = intent.getStringExtra("selectedClass").toString()
        MyApp.getInstance().getApiServices().getTeacherAttendance(teacherID, attendanceType)
            .enqueue(object: Callback<List<TeacherAttendanceItem>> {
            override fun onResponse(

                call: Call<List<TeacherAttendanceItem>>,
                response: Response<List<TeacherAttendanceItem>>

            )
            {

                if(response.isSuccessful && !response.body().isNullOrEmpty()){

                    val attendanceList: List<TeacherAttendanceItem>? = response.body()
                    Log.e("ml", attendanceList.toString())
                    Log.e("resp", response.body().toString())

                    //create the pie chart
                    pieChart=findViewById(R.id.pie_chart)
                    val list:ArrayList<PieEntry> = ArrayList()
                    var PCount : Int = 0

                    if (attendanceList != null) {

                        for (element in attendanceList) {

                            if (element.status == "P")
                                PCount += 1

                        }
                    }

                    val ACount : Int = (attendanceList?.size ?: 0) - PCount //absents count

                    list.add(PieEntry(PCount.toFloat(),"Presents"))
                    list.add(PieEntry(ACount.toFloat(),"Absents"))

                    val pieDataSet= PieDataSet(list,"")

                    pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS,255)
                    pieDataSet.valueTextColor= Color.BLACK
                    pieDataSet.valueTextSize=15f

                    val pieData= PieData(pieDataSet)

                    pieChart.data= pieData
                    pieChart.description.text= ""
                    pieChart.centerText=intent.getStringExtra("selectedClass").toString()

                    pieChart.animateY(2000)

                    //create the table
                    val recyclerView = findViewById<RecyclerView>(R.id.attendance_table)
                    val adapter = attendanceList?.let { TeacherAttendanceAdapter(it) }
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(this@ViewAttendance)


                }
                else {

                    Toast.makeText(applicationContext, "No attendance data!", Toast.LENGTH_LONG).show()

                }

            }

            override fun onFailure(call: Call<List<TeacherAttendanceItem>>, t: Throwable) {

                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
            }


        })


    }
}