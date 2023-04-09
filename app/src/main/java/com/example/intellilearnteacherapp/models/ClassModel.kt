package com.example.intellilearnteacherapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ClassModel(
    var class_ID:Int,
    var class_level : String,
    var section : String,
    var subject : String,
    var capacity : Int,
    var num_students : Int,
    var teacher_ID : Int) : Parcelable