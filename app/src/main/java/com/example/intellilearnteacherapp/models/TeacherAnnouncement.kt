package com.example.intellilearnteacherapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

data class TeacherAnnouncement(
    var announcement_ID:Int,
    var title: String,
    var date_posted : Date,
    var content: String,
    var author: TeacherModel,
    var class_ID: ClassModel)
