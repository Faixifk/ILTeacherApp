package com.example.intellilearnteacherapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.sql.Time
import java.time.DayOfWeek

@Parcelize
data class TeacherScheduleItem(
    var ID:Int,
    var weekday : String,
    var startTime : String,
    var durationMinutes : Int,
    var roomNumber : Int,
    var class_level: String,
    var section: String,
    var subject: String) : Parcelable
