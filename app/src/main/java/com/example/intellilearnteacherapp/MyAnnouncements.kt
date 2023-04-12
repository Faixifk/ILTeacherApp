package com.example.intellilearnteacherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.intellilearnteacherapp.models.ClassModel
import com.example.intellilearnteacherapp.models.TeacherAnnouncement
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

        // Initialize the RecyclerView and its adapter
        recyclerView = findViewById(R.id.announcementsRecyclerView)
        adapter = AnnouncementsAdapter(announcementsList)

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

                        }
                    }

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
            holder.authorName.text = announcement.author.data.name
            holder.classInfo.text = "${announcement.class_ID.class_level} ${announcement.class_ID.section} ${announcement.class_ID.subject}"
        }

        override fun getItemCount(): Int {
            return announcementsList.size
        }

        // View holder for each item in the list
        class AnnouncementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val announcementTitle: TextView = itemView.findViewById(R.id.announcementsTitle)
            val authorName: TextView = itemView.findViewById(R.id.announcementAuthorTextView)
            val classInfo: TextView = itemView.findViewById(androidx.transition.R.id.content)
        }
    }
}
