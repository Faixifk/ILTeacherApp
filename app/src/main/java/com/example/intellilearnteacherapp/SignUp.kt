package com.example.intellilearnteacherapp

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.intellilearnteacherapp.models.LoginResponse
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_sign_up.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import storage.SharedPrefManager
import java.text.SimpleDateFormat
import java.util.*

class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        textViewLogin.setOnClickListener{

            val intent = Intent(this@SignUp, LoginActivity::class.java)
            startActivity(intent)

        }

        editTextDOB.setOnClickListener {
            showDatePickerDialog(editTextDOB)
        }

        buttonSignUp.setOnClickListener {

            val name = editTextName.text.toString()
            val dob = editTextDOB.text.toString()
            val email = editTextEmail.text.toString()
            val passwordEditText = findViewById<TextInputEditText>(R.id.editTextPassword)
            val confirmPasswordEditText = findViewById<TextInputEditText>(R.id.editTextConfirmPassword)

            if (passwordsMatch(passwordEditText, confirmPasswordEditText)) {

                // Handle sign-up process

                MyApp.getInstance().getApiServices().registerTeacher(email, passwordEditText.text.toString(), name)
                    .enqueue(object: Callback<LoginResponse> {
                        override fun onResponse(
                            call: Call<LoginResponse>,
                            response: Response<LoginResponse>
                        ) {

                            if(response.body()?.response.toString() == "Success"){

                                SharedPrefManager.getInstance(applicationContext).saveUser(response.body()?.data!!)

                                val intent = Intent(applicationContext, NavigationScreenActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                                startActivity(intent)

                            }
                            else {

                                Toast.makeText(applicationContext, "User already exists!", Toast.LENGTH_LONG).show()

                            }

                        }

                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {

                            Toast.makeText(applicationContext, "Server did not respond!", Toast.LENGTH_LONG).show()
                        }


                    })

            } else {
                showPasswordsMismatchAlert()
            }
        }


    }

    private fun showPasswordsMismatchAlert() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Error")
        alertDialogBuilder.setMessage("Passwords do not match.")
        alertDialogBuilder.setPositiveButton("OK") { _, _ -> }
        alertDialogBuilder.show()
    }

    private fun showDatePickerDialog(dobEditText: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(selectedYear, selectedMonth, selectedDay)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dobEditText.setText(dateFormat.format(selectedDate.time))
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun passwordsMatch(passwordEditText: TextInputEditText, confirmPasswordEditText: TextInputEditText): Boolean {
        return passwordEditText.text.toString() == confirmPasswordEditText.text.toString()
    }
}