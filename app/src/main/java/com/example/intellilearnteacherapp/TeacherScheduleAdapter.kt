package com.example.intellilearnteacherapp

import android.graphics.Color.green
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.intellilearnteacherapp.models.TeacherScheduleItem
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class TeacherScheduleAdapter(private val scheduleList: List<TeacherScheduleItem>) :
    RecyclerView.Adapter<TeacherScheduleAdapter.ScheduleViewHolder>() {

    inner class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val weekday: TextView = itemView.findViewById(R.id.trWeekday)
        val startTime: TextView = itemView.findViewById(R.id.trStartTime)
        val durationMinutes: TextView = itemView.findViewById(R.id.trDurationMin)
        val roomNumber: TextView = itemView.findViewById(R.id.trRoomNumber)
        val Class: TextView = itemView.findViewById(R.id.trClass)
        val section: TextView = itemView.findViewById(R.id.trSection)
        val subject: TextView = itemView.findViewById(R.id.trSubject)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.schedule_row, parent, false)
        return ScheduleViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {

        val currentItem = scheduleList[position]

        holder.weekday.text = currentItem.weekday
        holder.startTime.text = currentItem.startTime.toString()
        holder.durationMinutes.text = currentItem.durationMinutes.toString()
        holder.roomNumber.text = currentItem.roomNumber.toString()
        holder.Class.text = currentItem.class_level
        holder.section.text = currentItem.section
        holder.subject.text = currentItem.subject

        when (currentItem.weekday.lowercase()) {
            LocalDate.now().dayOfWeek.name.lowercase() -> holder.weekday.setTextColor(ContextCompat.getColor(holder.weekday.context, R.color.green))
        }

    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }


}