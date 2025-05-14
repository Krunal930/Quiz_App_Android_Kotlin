package com.example.quiz_app_official

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AdminQuestionDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_question_details)

        findViewById<Button>(R.id.backButton).setOnClickListener {
            val intent = Intent(this, AdminQuestions::class.java)
            startActivity(intent)
        }
    }
}