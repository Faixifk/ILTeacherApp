package com.example.intellilearnteacherapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_new_mcq.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log

class AddNewMcqActivity : AppCompatActivity() {

    companion object{

        const val REQUEST_CODE_ADD_MCQ = 1000
        const val EXTRA_MCQ = "extra mcq"
        fun startActivity(activity : AppCompatActivity){

            val intent = Intent(activity, AddNewMcqActivity::class.java)
            activity.startActivityForResult(intent, REQUEST_CODE_ADD_MCQ)

        }

    }

    private val mcq = McqItem(-1, "", "", "", "", "", "", -1, -1, "")

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_mcq)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater

        menuInflater.inflate(R.menu.menu_add_mcq, menu)

        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {

            R.id.action_done -> {

                if(etQuestion.text.isNullOrEmpty()){

                    Toast.makeText(this, "Question field is missing!", Toast.LENGTH_LONG).show()
                    return true

                }

                if(etOptionA.text.isNullOrEmpty()){

                    Toast.makeText(this, "Option A field is missing!", Toast.LENGTH_LONG).show()
                    return true

                }

                if(etOptionB.text.isNullOrEmpty()){

                    Toast.makeText(this, "Option B field is missing!", Toast.LENGTH_LONG).show()
                    return true

                }

                if(etOptionC.text.isNullOrEmpty()){

                    Toast.makeText(this, "Option C field is missing!", Toast.LENGTH_LONG).show()
                    return true

                }

                if(etOptionD.text.isNullOrEmpty()){

                    Toast.makeText(this, "Option D field is missing!", Toast.LENGTH_LONG).show()
                    return true

                }

                if(etCorrectOption.text.isNullOrEmpty()){

                    Toast.makeText(this, "Correction Option field is missing!", Toast.LENGTH_LONG).show()
                    return true

                }

                if(etWeightage.text.isNullOrEmpty()){

                    Toast.makeText(this, "Weight field is missing!", Toast.LENGTH_LONG).show()
                    return true

                }

                if(etUnitNumber.text.isNullOrEmpty()){

                    Toast.makeText(this, "Unit Number field is missing!", Toast.LENGTH_LONG).show()
                    return true

                }

                if(etTopic.text.isNullOrEmpty()){

                    Toast.makeText(this, "Topic field is missing!", Toast.LENGTH_LONG).show()
                    return true

                }

                mcq.question = etQuestion.text.toString()
                mcq.option_a = etOptionA.text.toString()
                mcq.option_b = etOptionB.text.toString()
                mcq.option_c = etOptionC.text.toString()
                mcq.option_d = etOptionD.text.toString()
                mcq.correct_option = etCorrectOption.text.toString()
                mcq.weight = (etWeightage.text.toString()).toInt()
                mcq.unit_number = (etUnitNumber.text.toString()).toInt()
                mcq.topic = etTopic.text.toString()

                addMcqItemToServer()

                val data = Intent()
                data.putExtra(EXTRA_MCQ, mcq)
                setResult(Activity.RESULT_OK, data)
                finish()


            }
        }

        return super.onOptionsItemSelected(item)
    }

//from video

    private fun addMcqItemToServer() {

        progressBar.visibility = View.VISIBLE

        MyApp.getInstance().getApiServices().addMCQ(mcq).enqueue(object : Callback<McqItem>{


            override fun onResponse(call: Call<McqItem>, response: Response<McqItem>) {

                progressBar.visibility = View.GONE

                val data = Intent()
                data.putExtra(EXTRA_MCQ, mcq)
                setResult(Activity.RESULT_OK, data)
                finish()

            }

            override fun onFailure(call: Call<McqItem>, t: Throwable) {

                progressBar.visibility = View.GONE
                Toast.makeText(this@AddNewMcqActivity, "Failed to POST!", Toast.LENGTH_LONG).show()
                //Toast.makeText(this@AddNewMcqActivity, "response" + t.getStackTrace().toString(), Toast.LENGTH_LONG).show();
                Log.d("TAG","response: " + t.stackTrace.toString())
                Log.d("TAG 2","Message: " + t.message.toString())

            }

        })

    }

}