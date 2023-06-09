package com.example.intellilearnteacherapp

import com.example.intellilearnteacherapp.models.*
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface APIServices {


    @POST("addNote")
    fun addNote(@Body note:NoteItem) : Call<NoteItem>

    @GET("addNote")
    fun getAllNoteItems(@Query("user_ID") id : Int) : Call<List<NoteItem>>

    @DELETE("addNote")
    fun deleteNoteItem(@Query("note_ID") id : Int) : Call<DeleteResponse>

    @POST("addMcq")
    fun addMCQ(@Body mcq:McqItem) : Call<McqItem>

    @GET("addMcq")
    fun getAllMcqItems() : Call<List<McqItem>>

    @GET("teacherClasses")
    fun getTeacherClasses(@Query("teacher_ID") id : Int) : Call<List<ClassModel>>

    @GET("marksByEvaluationType")
    fun getMarksByEvaluationType(
        @Query("class_level") class_level: Int,
        @Query("section") section: String?,
        @Query("subject") subject: String?,
        @Query("evaluationType") evaluationType: String,
    ) : Call<List<MarksModel>>

    @POST("questionAnswering")
    fun askAI(
        @Query("question") question: String,
        @Query("class") Class: String?,
        @Query("chapter") chapter: String?,
    ) : Call<String>


    @FormUrlEncoded
    @POST("loginTeacher")
    fun loginTeacher(

        @Field("email") email:String,
        @Field("password") password:String,

    ) : Call<LoginResponse>

    @FormUrlEncoded
    @POST("addTeacher")
    fun registerTeacher(

        @Field("email") email:String,
        @Field("password") password:String,
        @Field("name") name:String,

        ) : Call<LoginResponse>

    @DELETE("addMcq")
    fun deleteMcqItem(@Query("question_ID") id : Int) : Call<DeleteResponse>

    @GET("askChatGPT")
    fun askChatGPT(@Query("question") question : String) : Call<String>

    //get teacher attendance for displaying
    @GET("teacherAttendance")
    fun getTeacherAttendance(@Query("teacher_ID") teacher_ID : Int
                             ,@Query("attendance_type") attendance_type : String
    ) : Call<List<TeacherAttendanceItem>>

    //get teacher schedule
    @GET("addTeacherSchedule")
    fun getTeacherSchedule(@Query("teacher_ID") teacher_ID : Int) : Call<List<TeacherScheduleItem>>

    @GET("teacherAnnouncement")
    fun getTeacherAnnouncements(@Query("author") teacher_ID : Int) : Call<List<TeacherAnnouncement>>

    @Multipart
    @POST("upload_book/")
    fun uploadBook(
        @Part("title") title: String,
        @Part("className") className: String,
        @Part file: MultipartBody.Part
    ): Call<ResponseBody>

    @GET("books/")
    fun getSpinner1Data(): Call<List<String>>

}

data class DeleteResponse ( var response : String)