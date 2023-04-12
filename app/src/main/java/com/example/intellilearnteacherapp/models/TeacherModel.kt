package com.example.intellilearnteacherapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize

data class TeacherModel(
    val teacher_ID: Int,
    val email: String,
    val password: String,
    val name: String):Parcelable

//data class TeacherData(
//    val teacher_ID: Int,
//    val email: String,
//    val password: String,
//    val name: String
//) : Parcelable