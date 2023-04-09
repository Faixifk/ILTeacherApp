package storage

//a class to contain the login information for longer time

import android.content.Context
import android.content.SharedPreferences
import com.example.intellilearnteacherapp.models.Teacher
import com.example.intellilearnteacherapp.models.TeacherScheduleItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class SharedPrefManager private constructor(private val mCtx: Context) {

    val isLoggedIn: Boolean
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getInt("teacher_ID", -1) != -1
        }

    val teacher: Teacher
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return Teacher(
                sharedPreferences.getInt("teacher_ID", -1),
                sharedPreferences.getString("name", null),
                sharedPreferences.getString("email", null)
            )
        }


    fun saveUser(teacher: Teacher) {

        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putInt("teacher_ID", teacher.teacher_ID)
        editor.putString("name", teacher.name)
        editor.putString("email", teacher.email)

        editor.apply()

    }

    fun clear() {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    companion object {
        private val SHARED_PREF_NAME = "my_shared_preff"
        private var mInstance: SharedPrefManager? = null
        @Synchronized
        fun getInstance(mCtx: Context): SharedPrefManager {
            if (mInstance == null) {
                mInstance = SharedPrefManager(mCtx)
            }
            return mInstance as SharedPrefManager
        }
    }

}