package com.example.intellilearnteacherapp

import com.example.intellilearnteacherapp.models.*
import retrofit2.Call
import retrofit2.http.*

interface APIServices {

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
        @Query("subject") subject: String?,
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


}

data class DeleteResponse ( var response : String)