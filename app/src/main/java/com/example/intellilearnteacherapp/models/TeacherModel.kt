package com.example.intellilearnteacherapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class TeacherModel(
    val response: String,
    val data: TeacherData
)

@Parcelize
data class TeacherData(
    val teacher_ID: Int,
    val email: String,
    val password: String,
    val name: String
) : Parcelable