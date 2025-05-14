package com.example.quiz_app_official

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class page4 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_page4)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val rootLayout = findViewById<LinearLayout>(R.id.main)

        rootLayout.setOnClickListener {
            val intent = Intent(this, UserHomeActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.backButton).setOnClickListener {
            val intent = Intent(this, UserHomeActivity::class.java)
            startActivity(intent)
        }

    }
}