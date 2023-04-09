package com.example.intellilearnteacherapp

import android.app.Activity
import android.content.Intent
import android.os.Build
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
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_add_new_mcq.progressBar
import kotlinx.android.synthetic.main.activity_add_new_note.*
import kotlinx.android.synthetic.main.activity_add_new_note.ntitle
import kotlinx.android.synthetic.main.table_row.*
import kotlinx.android.synthetic.main.table_row.view.*
import storage.SharedPrefManager
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class AddNewNote : AppCompatActivity() {

    companion object{

        const val REQUEST_CODE_ADD_NOTE = 1000
        const val EXTRA_NOTE = "extra note"
        fun startActivity(activity : AppCompatActivity){

            val intent = Intent(activity, AddNewNote::class.java)
            activity.startActivityForResult(intent, REQUEST_CODE_ADD_NOTE)

        }

    }

    private var note = NoteItem(-1, "", -1, "", Date())

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_note)

        note = intent.extras?.getParcelable("data")?: NoteItem(created_at = Date())

        ntitle.setText(note.title?:"")
        nContent.setText(note.content?:"")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater

        menuInflater.inflate(R.menu.menu_add_note, menu)

        return true

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {

            R.id.note_action_done -> {

                if(ntitle.text.isNullOrEmpty()){

                    Toast.makeText(this, "Title field is missing!", Toast.LENGTH_LONG).show()
                    return true

                }


                if(nContent.text.isNullOrEmpty()){

                    Toast.makeText(this, "Content field is missing!", Toast.LENGTH_LONG).show()
                    return true

                }



                note.title=ntitle.text.toString()

                // this creates a new Date object with the current date and time
                val currentDate = Date()
                note.created_at = currentDate

                note.content=nContent.text.toString()
                note.user=SharedPrefManager.getInstance(applicationContext).teacher.teacher_ID





                addNoteItemToServer()

                val data = Intent()
                data.putExtra(EXTRA_NOTE, note)
                setResult(RESULT_OK, data)
                finish()


            }
        }

        return super.onOptionsItemSelected(item)
    }

//from video

    private fun addNoteItemToServer() {

        progressBar.visibility = View.VISIBLE

        MyApp.getInstance().getApiServices().addNote(note).enqueue(object : Callback<NoteItem>{


            override fun onResponse(call: Call<NoteItem>, response: Response<NoteItem>) {

                progressBar.visibility = View.GONE

                val data = Intent()
                data.putExtra(EXTRA_NOTE, note)
                setResult(RESULT_OK, data)
                finish()

            }

            override fun onFailure(call: Call<NoteItem>, t: Throwable) {

                progressBar.visibility = View.GONE
                Toast.makeText(this@AddNewNote, "Failed to POST!", Toast.LENGTH_LONG).show()
                //Toast.makeText(this@AddNewNoteActivity, "response" + t.getStackTrace().toString(), Toast.LENGTH_LONG).show();
                Log.d("TAG","response: " + t.stackTrace.toString())
                Log.d("TAG 2","Message: " + t.message.toString())

            }

        })

    }

}