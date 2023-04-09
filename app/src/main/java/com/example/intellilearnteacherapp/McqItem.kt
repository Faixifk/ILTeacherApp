package com.example.intellilearnteacherapp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class McqItem(
    var question_ID:Int,
    var question : String,
    var option_a : String,
    var option_b : String,
    var option_c : String,
    var option_d : String,
    var correct_option : String,
    var weight : Int,
    var unit_number : Int,
    var topic : String ) : Parcelable

