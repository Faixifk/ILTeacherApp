package com.example.intellilearnteacherapp

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.intellilearnteacherapp.models.MarksModel
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.activity_ask_ai.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//activity to provide interface to backend API using BERT for question answering
class AskAI : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ask_ai)

        progressBar.visibility = View.GONE

        val editText: EditText = findViewById(R.id.editText)
        val spinner1: Spinner = findViewById(R.id.spinner1)
        val spinner2: Spinner = findViewById(R.id.spinner2)
        val spinner3: Spinner = findViewById(R.id.spinner3)
        val textView: TextView = findViewById(R.id.textView)
        val submitButton: Button = findViewById(R.id.submit_button)

        // Set default values for Spinner 1
        val adapter1 = ArrayAdapter.createFromResource(this, R.array.dropdown1_values, android.R.layout.simple_spinner_item)
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner1.adapter = adapter1

        // Set default values for Spinner 2 based on Spinner 1
        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val arrayId = when (position) {
                    0 -> R.array.dropdown2_values_option1
                    1 -> R.array.dropdown2_values_option2
                    else -> R.array.dropdown2_values_option1
                }
                val adapter2 = ArrayAdapter.createFromResource(this@AskAI, arrayId, android.R.layout.simple_spinner_item)
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner2.adapter = adapter2

                // Set default values for Spinner 3 based on Spinner 2
                spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                        val arrayId = when (position) {
                            0 -> R.array.dropdown3_values_option1
                            1 -> R.array.dropdown3_values_option2
                            else -> R.array.dropdown3_values_option1
                        }
                        val adapter3 = ArrayAdapter.createFromResource(this@AskAI, arrayId, android.R.layout.simple_spinner_item)
                        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinner3.adapter = adapter3
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }


                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        // Enable submit button only if the text box has some value
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                submitButton.isEnabled = s.isNotEmpty()
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing
            }
        })

        // Set onClickListener for the submit button
        submitButton.setOnClickListener {
            // Implement your desired action on button click

            progressBar.visibility = View.VISIBLE

            MyApp.getInstance().getApiServices().askAI(editText.text.toString(), spinner1.selectedItem.toString(), spinner2.selectedItem.toString(), spinner3.selectedItem.toString()).enqueue(object :
                Callback<String> {

                override fun onResponse(call: Call<String>, response: Response<String>) {

                    progressBar.visibility = View.GONE

                    if (response.isSuccessful && !response.body().isNullOrEmpty()){

                        //now extract data
                        val answer: String? = response.body()
                        if (answer != null) {
                            Log.e("answer:", answer)
                        }

                        textView.text = answer

                    }
                    else{

                        Toast.makeText(this@AskAI, "Error getting answer from BERT", Toast.LENGTH_LONG).show()

                    }

                }

                override fun onFailure(call: Call<String>, t: Throwable) {

                    progressBar.visibility = View.GONE

                    Toast.makeText(this@AskAI, "No response from server!", Toast.LENGTH_LONG).show()

                }


            })


        }
    }
}
