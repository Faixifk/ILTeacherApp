package com.example.intellilearnteacherapp

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


public class MainActivity : AppCompatActivity(), AdapterMcqItem.OnDeleteMcqClickListener {

    private val mcqItemAdapter = AdapterMcqItem()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mcqItemAdapter
        mcqItemAdapter.onDeleteMcqClickListener = this@MainActivity

        faAddNewMcq.setOnClickListener {
            //Toast.makeText(this, "Clicked", Toast.LENGTH_LONG)
            AddNewMcqActivity.startActivity(this)

        }

        progressBar.visibility = View.VISIBLE

        MyApp.getInstance().getApiServices().getAllMcqItems().enqueue(object : Callback<List<McqItem>>{

            override fun onResponse(call: Call<List<McqItem>>, response: Response<List<McqItem>>) {

                progressBar.visibility = View.GONE


                if (response.isSuccessful && !response.body().isNullOrEmpty()){

                    mcqItemAdapter.setList(response.body() as ArrayList<McqItem>)
                }
                else{

                    Toast.makeText(this@MainActivity, "Error loading MCQ list", Toast.LENGTH_LONG).show()

                }

            }

            override fun onFailure(call: Call<List<McqItem>>, t: Throwable) {

                progressBar.visibility = View.GONE
                Toast.makeText(this@MainActivity, "Error loading MCQ list", Toast.LENGTH_LONG).show()

            }


        })


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){

            if(requestCode == AddNewMcqActivity.REQUEST_CODE_ADD_MCQ){

                data?.let {

                    val item = data.getParcelableExtra<McqItem>(AddNewMcqActivity.EXTRA_MCQ)
                    mcqItemAdapter.addItem(item!!)
                    val toneGen1 = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
                    toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150)

                }


            }
        }
    }

    override fun onDeleteMcq(mcq: McqItem, position: Int) {


        AlertDialog.Builder(this@MainActivity)
            .setMessage("Are you sure you want to delete this MCQ?")
            .setNegativeButton("No"){ dialogInterface: DialogInterface, i: Int ->

                dialogInterface.dismiss()

            }
            .setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->

                dialogInterface.dismiss()
                progressBar.visibility = View.VISIBLE

                MyApp.getInstance().getApiServices().deleteMcqItem(mcq.question_ID)
                    .enqueue(object : Callback<DeleteResponse> {
                        override fun onResponse(
                            call: Call<DeleteResponse>,
                            response: Response<DeleteResponse>
                        ) {

                            progressBar.visibility = View.GONE
                            if (response.isSuccessful && response.body() != null) {

                                Toast.makeText(
                                    this@MainActivity,
                                    response.body()?.response,
                                    Toast.LENGTH_LONG
                                ).show()
                                mcqItemAdapter.deleteMcq(position)


                            } else {

                                Toast.makeText(
                                    this@MainActivity,
                                    "Error deleting MCQ",
                                    Toast.LENGTH_LONG
                                ).show()


                            }

                        }

                        override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {

                            progressBar.visibility = View.GONE
                            Toast.makeText(
                                this@MainActivity,
                                "Error deleting MCQ",
                                Toast.LENGTH_LONG
                            ).show()

                        }


                    })

            }
            .show()


    }

}