package com.example.intellilearnteacherapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_ask_ai.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AskAI : AppCompatActivity() {
    private lateinit var spinner1: Spinner
    private lateinit var spinner3: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ask_ai)

        progressBar.visibility = View.GONE

        val editText: EditText = findViewById(R.id.editText)
        spinner1 = findViewById(R.id.spinner1)
        spinner3 = findViewById(R.id.spinner3)
        val answerTextView: TextView = findViewById(R.id.answerTextView)
        val submitButton: Button = findViewById(R.id.submit_button)

        // Set default values for Spinner 3 based on Spinner 1
        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                loadSpinner3Data(position)
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
            answerTextView.text = ""


            MyApp.getInstance().getApiServices().askAI(
                editText.text.toString(),
                spinner1.selectedItem.toString(),
                spinner3.selectedItem.toString()
            ).enqueue(object :
                Callback<String> {

                override fun onResponse(call: Call<String>, response: Response<String>) {

                    progressBar.visibility = View.GONE

                    if (response.isSuccessful && !response.body().isNullOrEmpty()) {

                        //now extract data
                        val answer: String? = response.body()
                        if (answer != null) {
                            Log.e("answer:", answer)
                        }

                        answerTextView.text = answer

                    } else {

                        Toast.makeText(
                            this@AskAI,
                            "Error getting answer from BERT",
                            Toast.LENGTH_LONG
                        ).show()

                    }

                }

                override fun onFailure(call: Call<String>, t: Throwable) {

                    progressBar.visibility = View.GONE

                    Toast.makeText(this@AskAI, "No response from server!", Toast.LENGTH_LONG).show()

                }


            })


        }

        // Fetch spinner1 data from API
        fetchSpinner1Data()
    }

    private fun fetchSpinner1Data() {
        // Make an API call to fetch spinner1 data
        MyApp.getInstance().getApiServices().getSpinner1Data().enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful && response.body() != null) {
                    val spinner1Data = response.body()
                    if (spinner1Data != null) {
                        val adapter1 = ArrayAdapter(
                            this@AskAI,
                            android.R.layout.simple_spinner_item,
                            spinner1Data
                        )
                        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinner1.adapter = adapter1
                    }
                } else {
                    Toast.makeText(
                        this@AskAI,
                        "Error fetching spinner1 data",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Toast.makeText(
                    this@AskAI,
                    "Error fetching spinner1 data",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun loadSpinner3Data(position: Int) {
        // Load Spinner3 data based on Spinner1 selection
        val arrayId = when (position) {
            0 -> R.array.dropdown3_values_option1
            1 -> R.array.dropdown3_values_option2
            else -> R.array.dropdown3_values_option1
        }
        val adapter3 = ArrayAdapter.createFromResource(
            this@AskAI,
            arrayId,
            android.R.layout.simple_spinner_item
        )
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner3.adapter = adapter3
    }
}
