package com.example.intellilearnteacherapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class MarksModel(
    var marks_ID:Int,
    var date : Date,
    var evaluationType : String,
    var totalMarks : Int,
    var obtainedMarks : Int,
    var class_ID : Int,
    var student_ID : Int) : Parcelable
