package com.example.quiz_app_official

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class pages_aboutus : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pages_aboutus)

        val buttonBack: Button = findViewById(R.id.buttonBack)
        buttonBack.bringToFront()


        findViewById<Button>(R.id.buttonBack).setOnClickListener {
            val intent = Intent(this, UserHomeActivity::class.java)
            startActivity(intent)
        }
    }

}