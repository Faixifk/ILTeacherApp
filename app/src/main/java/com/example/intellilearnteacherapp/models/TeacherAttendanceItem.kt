package com.example.intellilearnteacherapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class TeacherAttendanceItem(
    var attendance_ID:Int,
    var date : Date,
    var status : String,
    var class_level : String,
    var section : String,
    var subject: String) : Parcelable
