package com.example.intellilearnteacherapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_upload_book.*
import kotlinx.android.synthetic.main.activity_upload_book.view.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UploadBook : AppCompatActivity() {
    private lateinit var uploadButton: Button
    private lateinit var submitButton: Button
    private lateinit var fileName: TextView
    private var fileUri: Uri? = null
    private lateinit var progressBar: ProgressBar

    companion object {
        const val PICK_PDF_FILE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_book)

        progressBar = findViewById(R.id.progressBar)


        uploadButton = findViewById(R.id.upload_button)
        submitButton = findViewById(R.id.submit_button)
        fileName = findViewById(R.id.file_name)

        uploadButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/pdf"
            }
            startActivityForResult(intent, PICK_PDF_FILE)
        }

        submitButton.setOnClickListener {
            if (fileUri != null) {
                // Upload the file to your server here

                progressBar.visibility = View.VISIBLE

                uploadFile(fileUri!!)
                Toast.makeText(this, "Uploading file...", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please choose a file first.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_PDF_FILE && resultCode == Activity.RESULT_OK) {
            data?.data?.also { uri ->
                fileUri = uri

                val fileNameString = getFileName(uri)
                //val fileNameString = uri.path ?: "Unknown"
                fileName.text = fileNameString

            }
        }
    }


    private fun uploadFile(uri: Uri) {
        val contentResolver = this.contentResolver
        val fileSize = contentResolver.openFileDescriptor(uri, "r")?.statSize ?: 0L
        val inputStream = contentResolver.openInputStream(uri) ?: return
        val fileNameString = getFileName(uri)
        val contentType = "application/pdf".toMediaTypeOrNull()

        val progressRequestBody = ProgressRequestBody(inputStream, fileSize, contentType, object : ProgressRequestBody.UploadProgressListener {
            override fun onProgressUpdate(percentage: Int) {
                runOnUiThread { progressBar.progress = percentage }
            }
        })

        val filePart = MultipartBody.Part.createFormData("pdf_file", fileNameString, progressRequestBody)
        val title = title_layout.editTextTitle.text.toString() // Get the title from the EditText field
        val classNameStr = class_layout.editTextClass.text.toString() // Get the title from the EditText field

        MyApp.getInstance().getApiServices().uploadBook(title, classNameStr, filePart).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@UploadBook, "File uploaded successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@UploadBook, "File upload failed", Toast.LENGTH_SHORT).show()
                    Log.e("UploadBook", "Error response: ${response.errorBody()?.string()}")

                }
                progressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@UploadBook, t.message, Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
            }
        })
    }

    @SuppressLint("Range")
    private fun getFileName(uri: Uri): String {
        var result: String? = null
        val cursor = contentResolver.query(uri, null, null, null, null)
        try {
            if (cursor != null && cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        } finally {
            cursor?.close()
        }
        return result ?: "unknown.pdf"
    }

}