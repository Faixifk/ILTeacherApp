package com.example.intellilearnteacherapp

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.intellilearnteacherapp.models.MarksModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type


class SampleBarPLot : AppCompatActivity() {

    lateinit var barChart:BarChart
    lateinit var pieChart: PieChart
    lateinit var radarChart: RadarChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_bar_plot)

        //get data from server

        //first get data from button values from last activity to know what data to plot
        val buttonText = intent.getStringExtra("buttonText")
        val words = buttonText?.split("\\s".toRegex())?.toTypedArray()
        val class_level = words?.get(1)
        val section = words?.get(3)
        val subject = words?.get(4)
        val evaluationType = "Quiz 1"



        MyApp.getInstance().getApiServices().getMarksByEvaluationType(class_level?.toInt() ?: -1, section, subject, evaluationType).enqueue(object :
            Callback<List<MarksModel>> {

            override fun onResponse(call: Call<List<MarksModel>>, response: Response<List<MarksModel>>) {

                if (response.isSuccessful && !response.body().isNullOrEmpty()){

                    //now extract data
                    val marksList: List<MarksModel>? = response.body()
                    Log.e("ml", marksList.toString())
                    barChart=findViewById(R.id.bar_chart)


                    val list: ArrayList<BarEntry> = ArrayList()
                    if (marksList != null) {
                        for (element in marksList) {

                            list.add(BarEntry(element.obtainedMarks.toFloat(), element.obtainedMarks.toFloat()))
                        }
                    }


//                    list.add(BarEntry(100f,100f))
//                    list.add(BarEntry(101f,200f))
//                    list.add(BarEntry(102f,300f))
//                    list.add(BarEntry(103f,400f))
//                    list.add(BarEntry(104f,500f))


                    val barDataSet= BarDataSet(list,"List")

                    barDataSet.setColors(ColorTemplate.MATERIAL_COLORS,255)
                    barDataSet.valueTextColor=Color.BLACK

                    val barData= BarData(barDataSet)

                    barChart.setFitBars(true)

                    barChart.data= barData

                    barChart.description.text= "Bar Chart"

                    barChart.animateY(2000)

                }
                else{

                    Toast.makeText(this@SampleBarPLot, "Error loading Marks data", Toast.LENGTH_LONG).show()

                }

            }

            override fun onFailure(call: Call<List<MarksModel>>, t: Throwable) {

                Toast.makeText(this@SampleBarPLot, "No response from server!", Toast.LENGTH_LONG).show()

            }


        })

        pieChart=findViewById(R.id.pie_chart)


        val list2:ArrayList<PieEntry> = ArrayList()

        list2.add(PieEntry(100f,"100"))
        list2.add(PieEntry(101f,"101"))
        list2.add(PieEntry(102f,"102"))
        list2.add(PieEntry(103f,"103"))
        list2.add(PieEntry(104f,"104"))

        val pieDataSet= PieDataSet(list2,"List")

        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS,255)
        pieDataSet.valueTextColor= Color.BLACK
        pieDataSet.valueTextSize=15f

        val pieData= PieData(pieDataSet)

        pieChart.data= pieData

        pieChart.description.text= "Pie Chart"

        pieChart.centerText="List"

        pieChart.animateY(2000)


        radarChart= findViewById(R.id.radar_chart)

        val list3:ArrayList<RadarEntry> = ArrayList()

        list3.add(RadarEntry(100f))
        list3.add(RadarEntry(101f))
        list3.add(RadarEntry(102f))
        list3.add(RadarEntry(103f))
        list3.add(RadarEntry(104f))

        val radarDataSet=RadarDataSet(list3,"List")

        radarDataSet.setColors(ColorTemplate.MATERIAL_COLORS,255)

        radarDataSet.lineWidth=2f

        radarDataSet.valueTextColor = Color.RED

        radarDataSet.valueTextSize=14f

        val radarData=RadarData()

        radarData.addDataSet(radarDataSet)

        radarChart.data=radarData


        radarChart.description.text= "Radar Chart"


        radarChart.animateY(2000)


    }
}