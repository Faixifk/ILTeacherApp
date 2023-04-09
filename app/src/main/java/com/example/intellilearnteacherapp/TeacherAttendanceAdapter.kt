package com.example.intellilearnteacherapp

import android.graphics.Color.green
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.intellilearnteacherapp.models.TeacherAttendanceItem
import java.text.SimpleDateFormat
import java.util.*

class TeacherAttendanceAdapter(private val attendanceList: List<TeacherAttendanceItem>) :
    RecyclerView.Adapter<TeacherAttendanceAdapter.AttendanceViewHolder>() {

    inner class AttendanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val attendanceId: TextView = itemView.findViewById(R.id.trAttendance_ID)
        val date: TextView = itemView.findViewById(R.id.trDate)
        val status: TextView = itemView.findViewById(R.id.trStatus)
        val classLevel: TextView = itemView.findViewById(R.id.trClass_level)
        val section: TextView = itemView.findViewById(R.id.trSection)
        val subject: TextView = itemView.findViewById(R.id.trSubject)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.table_row, parent, false)
        return AttendanceViewHolder(view)
    }

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        val currentItem = attendanceList[position]

        holder.attendanceId.text = currentItem.attendance_ID.toString()
        //holder.date.text = currentItem.date.toString() //prints time and day also

        val outputDateFormat = SimpleDateFormat("yy-MM-dd", Locale.getDefault())
        holder.date.text = outputDateFormat.format(currentItem.date)

        holder.status.text = currentItem.status

        when (currentItem.status) {
            "P" -> holder.status.setTextColor(ContextCompat.getColor(holder.status.context, R.color.green))
            "A" -> holder.status.setTextColor(ContextCompat.getColor(holder.status.context, R.color.red))
        }

        holder.classLevel.text = currentItem.class_level
        holder.section.text = currentItem.section
        holder.subject.text = currentItem.subject
    }

    override fun getItemCount(): Int {
        return attendanceList.size
    }
}