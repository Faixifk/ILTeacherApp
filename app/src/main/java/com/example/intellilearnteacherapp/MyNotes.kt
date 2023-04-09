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
import com.example.intellilearnteacherapp.models.ClassModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.progressBar
import kotlinx.android.synthetic.main.activity_main.recyclerView
import kotlinx.android.synthetic.main.activity_my_notes.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import storage.SharedPrefManager


class MyNotes : AppCompatActivity(), AdapterNoteItem.OnDeleteNoteClickListener {

    private val noteItemAdapter = AdapterNoteItem()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_notes)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = noteItemAdapter
        noteItemAdapter.onDeleteNoteClickListener = this@MyNotes

        faAddNewNote.setOnClickListener {
            //Toast.makeText(this, "Clicked", Toast.LENGTH_LONG)
            AddNewNote.startActivity(this)
        }




        progressBar.visibility = View.VISIBLE

        MyApp.getInstance().getApiServices().getAllNoteItems(SharedPrefManager.getInstance(applicationContext).teacher.teacher_ID).enqueue(object :
            Callback<List<NoteItem>> {

            override fun onResponse(call: Call<List<NoteItem>>, response: Response<List<NoteItem>>) {

                progressBar.visibility = View.GONE


                if (response.isSuccessful && !response.body().isNullOrEmpty()){

                    noteItemAdapter.setList(response.body() as ArrayList<NoteItem>)
                }
                else{

                    Toast.makeText(this@MyNotes, "No Note in list", Toast.LENGTH_LONG).show()

                }

            }

            override fun onFailure(call: Call<List<NoteItem>>, t: Throwable) {

                progressBar.visibility = View.GONE
                Toast.makeText(this@MyNotes, "Error loading Note list", Toast.LENGTH_LONG).show()

            }


        })


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK){

            if(requestCode == AddNewNote.REQUEST_CODE_ADD_NOTE){

                data?.let {

                    val item = data.getParcelableExtra<NoteItem>(AddNewNote.EXTRA_NOTE)
                    noteItemAdapter.addItem(item!!)
                    val toneGen1 = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
                    toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150)

                }


            }
        }
    }


    override fun onDeleteNote(note: NoteItem, position: Int) {


        AlertDialog.Builder(this@MyNotes)
            .setMessage("Are you sure you want to delete this NOTE?")
            .setNegativeButton("No"){ dialogInterface: DialogInterface, i: Int ->

                dialogInterface.dismiss()

            }
            .setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->

                dialogInterface.dismiss()
                progressBar.visibility = View.VISIBLE

                MyApp.getInstance().getApiServices().deleteNoteItem(note.note_ID?:0)
                    .enqueue(object : Callback<DeleteResponse> {
                        override fun onResponse(
                            call: Call<DeleteResponse>,
                            response: Response<DeleteResponse>
                        ) {

                            progressBar.visibility = View.GONE
                            if (response.isSuccessful && response.body() != null) {

                                Toast.makeText(
                                    this@MyNotes,
                                    response.body()?.response,
                                    Toast.LENGTH_LONG
                                ).show()
                                noteItemAdapter.deleteNote(position)


                            } else {

                                Toast.makeText(
                                    this@MyNotes,
                                    "Error deleting Note",
                                    Toast.LENGTH_LONG
                                ).show()


                            }

                        }

                        override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {

                            progressBar.visibility = View.GONE
                            Toast.makeText(
                                this@MyNotes,
                                "Error deleting Note",
                                Toast.LENGTH_LONG
                            ).show()

                        }


                    })

            }
            .show()


    }

    override fun onEditNote(note: NoteItem) {

        val intent = Intent(this , AddNewNote::class.java)
        val bundle = Bundle()
        bundle.putParcelable("data", note)
        intent.putExtras(bundle)
        startActivity(intent)
    }

}