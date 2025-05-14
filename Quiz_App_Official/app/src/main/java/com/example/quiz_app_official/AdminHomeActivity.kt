package com.example.quiz_app_official

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminHomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)


        // Logout button
        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            val sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
            sharedPreferences.edit().clear().apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

//        findViewById<Button>(R.id.btnUploadExcel).setOnClickListener {
//            val intent = Intent(this, AdminUploadQuestions::class.java)
//            startActivity(intent)
//        }

        findViewById<Button>(R.id.btnQuestions).setOnClickListener {
            val intent = Intent(this, AdminQuestions::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnManageUsers).setOnClickListener {
            val intent = Intent(this, AdminManageUsers::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnAddManualQuestions).setOnClickListener {
            val intent = Intent(this, AdminAddManualQuestion::class.java)
            startActivity(intent)
        }
    }
}
