package com.example.intellilearnteacherapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.intellilearnteacherapp.models.ClassModel
import com.example.intellilearnteacherapp.models.TeacherAnnouncement
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import storage.SharedPrefManager

class MyAnnouncements : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AnnouncementsAdapter
    private val announcementsList = mutableListOf<TeacherAnnouncement>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_announcements)

        // Initialize FloatingActionButton
        val addAnnouncementFAB: FloatingActionButton = findViewById(R.id.addAnnouncementFAB)
        addAnnouncementFAB.setOnClickListener {
        // Start AddAnnouncementActivity when FloatingActionButton is clicked
            val intent = Intent(this, AddAnnouncementActivity::class.java)
            startActivity(intent)
        }

        // Initialize the RecyclerView, its adapter, and its layout manager
        recyclerView = findViewById(R.id.announcementsRecyclerView)
        adapter = AnnouncementsAdapter(announcementsList)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set the RecyclerView's adapter
        recyclerView.adapter = adapter

        // Fetch the list of TeacherAnnouncement objects from the server
        fetchAnnouncementsFromServer()
    }

    private fun fetchAnnouncementsFromServer() {

        MyApp.getInstance().getApiServices().getTeacherAnnouncements(SharedPrefManager.getInstance(applicationContext).teacher.teacher_ID).enqueue(object :
            Callback<List<TeacherAnnouncement>> {

            override fun onResponse(call: Call<List<TeacherAnnouncement>>, response: Response<List<TeacherAnnouncement>>) {

                if (response.isSuccessful && !response.body().isNullOrEmpty()){

                    //now extract data
                    val announcements: List<TeacherAnnouncement>? = response.body()

                    if (announcements != null) {
                        for (announcement in announcements) {

                            announcementsList.add(announcement)
                            Toast.makeText(this@MyAnnouncements, announcement.content, Toast.LENGTH_LONG).show()
                        }
                    }
                    adapter.notifyDataSetChanged()

                }
                else{

                    Toast.makeText(this@MyAnnouncements, "Error loading Announcements for Teacher", Toast.LENGTH_LONG).show()
                }

            }

            override fun onFailure(call: Call<List<TeacherAnnouncement>>, t: Throwable) {

                //Toast.makeText(this@MyAnnouncements, "Error loading announcements", Toast.LENGTH_LONG).show()
                Toast.makeText(this@MyAnnouncements, t.message.toString(), Toast.LENGTH_LONG).show()

            }


        })


    }

    // Custom adapter for displaying the list of TeacherAnnouncement objects
    class AnnouncementsAdapter(private val announcementsList: List<TeacherAnnouncement>) :
        RecyclerView.Adapter<AnnouncementsAdapter.AnnouncementViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.announcement_item, parent, false)
            return AnnouncementViewHolder(view)
        }

        override fun onBindViewHolder(holder: AnnouncementViewHolder, position: Int) {
            val announcement = announcementsList[position]

            // Set the views in the announcement_item.xml layout based on the announcement data
            holder.announcementTitle.text = announcement.title
            holder.authorName.text = announcement.author.name
            holder.classInfo.text = "${announcement.class_ID.class_level} ${announcement.class_ID.section} ${announcement.class_ID.subject}"
            holder.content.text = announcement.content
            holder.date.text = announcement.date_posted.toString()
        }

        override fun getItemCount(): Int {
            return announcementsList.size
        }

        // View holder for each item in the list
        class AnnouncementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val announcementTitle: TextView = itemView.findViewById(R.id.announcementTitleValueTextView)
            val authorName: TextView = itemView.findViewById(R.id.announcementAuthorValueTextView)
            val classInfo: TextView = itemView.findViewById(R.id.announcementClassValueTextView)
            val content: TextView = itemView.findViewById(R.id.announcementContentValueTextView)
            val date: TextView = itemView.findViewById(R.id.announcementDateValueTextView)

        }
    }
}
