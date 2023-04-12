package com.example.intellilearnteacherapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AddAnnouncementActivity : AppCompatActivity() {
    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var classEditText: EditText
    private lateinit var sectionEditText: EditText
    private lateinit var subjectEditText: EditText
    private lateinit var saveAnnouncementButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_announcement)

        titleEditText = findViewById(R.id.titleEditText)
        contentEditText = findViewById(R.id.contentEditText)
        classEditText = findViewById(R.id.classEditText)
        sectionEditText = findViewById(R.id.sectionEditText)
        subjectEditText = findViewById(R.id.subjectEditText)
        saveAnnouncementButton = findViewById(R.id.saveAnnouncementButton)

        saveAnnouncementButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val author = contentEditText.text.toString()
            val className = classEditText.text.toString()
            val section = sectionEditText.text.toString()
            val subject = subjectEditText.text.toString()

            // Validate input fields
            if (title.isBlank() || author.isBlank() || className.isBlank() || section.isBlank() || subject.isBlank()) {
                // Show error message (consider using a Snackbar or Toast)
                return@setOnClickListener
            }

            // TODO: Create an Announcement object and send it to the server
            // You can use the provided API call to save the announcement

            // Once the announcement is saved, finish this activity
            finish()
        }
    }
}
