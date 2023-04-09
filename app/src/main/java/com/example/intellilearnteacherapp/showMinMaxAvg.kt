package com.example.intellilearnteacherapp

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.intellilearnteacherapp.models.MarksModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class showMinMaxAvg : AppCompatActivity() {

    lateinit var barChart: BarChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_min_max_avg)


        //get data from server

        //first get data from button values from last activity to know what data to plot
        val buttonText = intent.getStringExtra("buttonText")
        val words = buttonText?.split("\\s".toRegex())?.toTypedArray()
        val classLevel = words?.get(1)
        val section = words?.get(3)
        val subject = words?.get(4)
        val evaluationType = "Quiz 1"

        MyApp.getInstance().getApiServices().getMarksByEvaluationType(classLevel?.toInt() ?: -1, section, subject, evaluationType).enqueue(object :
            Callback<List<MarksModel>> {

            override fun onResponse(call: Call<List<MarksModel>>, response: Response<List<MarksModel>>) {

                if (response.isSuccessful && !response.body().isNullOrEmpty()){

                    //now extract data
                    val marksList: List<MarksModel>? = response.body()
                    Log.e("ml", marksList.toString())
                    barChart=findViewById(R.id.bar_chart)

                    var maxMarks = marksList?.get(0)?.obtainedMarks
                    var minMarks = marksList?.get(0)?.obtainedMarks
                    var avgMarks = 0

                    if (marksList != null) {
                        for (marks in marksList){

                            if (maxMarks != null) {
                                if (maxMarks < marks.obtainedMarks)
                                    maxMarks = marks.obtainedMarks
                            }

                            if (minMarks != null) {
                                if (minMarks > marks.obtainedMarks)
                                    minMarks = marks.obtainedMarks
                            }

                            avgMarks += marks.obtainedMarks
                        }
                    }

                    avgMarks /= marksList?.size ?: 1

                    val list: ArrayList<BarEntry> = ArrayList()

                    if (minMarks != null) {
                        list.add(BarEntry(minMarks.toFloat(), minMarks.toFloat()))
                    }
                    list.add(BarEntry(avgMarks.toFloat(), avgMarks.toFloat()))
                    if (maxMarks != null) {
                        list.add(BarEntry(maxMarks.toFloat(), maxMarks.toFloat()))
                    }

                    val barDataSet= BarDataSet(list,"List")

                    barDataSet.setColors(ColorTemplate.MATERIAL_COLORS,255)
                    barDataSet.valueTextColor= Color.BLACK
                    barDataSet.colors = listOf(
                        Color.RED,
                        Color.YELLOW,
                        Color.GREEN
                    )

                    val barData= BarData(barDataSet)

                    // scaling can now only be done on x- and y-axis separately
                    barChart.setPinchZoom(false);

                    barChart.setDrawGridBackground(false)

                    val l: Legend = barChart.legend
                    l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                    l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                    l.orientation = Legend.LegendOrientation.HORIZONTAL
                    l.setDrawInside(false)
                    l.form = LegendForm.LINE
                    l.formSize = 9f
                    l.textSize = 11f
                    l.xEntrySpace = 4f


                    val colors = barDataSet.colors
                    val legendEntries: ArrayList<LegendEntry> = ArrayList()

                    legendEntries.add(LegendEntry("Min", LegendForm.CIRCLE, 10f, 2f, null, colors.get(0)))
                    legendEntries.add(LegendEntry("Avg", LegendForm.CIRCLE, 10f, 2f, null, colors.get(1)))
                    legendEntries.add(LegendEntry("Max", LegendForm.CIRCLE, 10f, 2f, null, colors.get(2)))

                    l.setCustom(legendEntries)

                    l.isEnabled = true

                    barChart.setFitBars(true)

                    barChart.data= barData

                    barChart.description.text= "Students Score Stats"
                    barChart.description.setPosition(300f,100f)
                    barChart.setDrawGridBackground(false)
//                    barChart.axisLeft.setDrawGridLines(false)
//                    barChart.axisRight.setDrawGridLines(false)
//                    barChart.xAxis.setDrawGridLines(false)
                    barChart.axisRight.setDrawLabels(false)
                    barChart.xAxis.setDrawLabels(false)

                    barChart.animateY(2000)

                }
                else{

                    Toast.makeText(this@showMinMaxAvg, "Error loading Marks data", Toast.LENGTH_LONG).show()

                }

            }

            override fun onFailure(call: Call<List<MarksModel>>, t: Throwable) {

                Toast.makeText(this@showMinMaxAvg, "No response from server!", Toast.LENGTH_LONG).show()

            }


        })
    }
}