package com.example.intellilearnteacherapp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat
import java.time.LocalDate
import java.util.Date

@Parcelize
data class NoteItem(
    var note_ID:Int?=0,
    var title: String?="",
    var user:Int?=0,
    var content: String?="",
    var created_at: Date, ): Parcelable

//
//note_ID	= models.AutoField(primary_key=True)
//title = models.CharField(max_length=255)
//content = models.TextField()
//created_at = models.DateTimeField(auto_now_add=True)

