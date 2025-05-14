package com.example.quiz_app_official

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var loginBtn: Button
    private lateinit var registerBtn: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference.child("Users")

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        loginBtn = findViewById(R.id.login_button)
        registerBtn = findViewById(R.id.register_button)

        sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE)

        // Check if user is already logged in
        checkUserLoggedIn()

        loginBtn.setOnClickListener {
            val emailStr = email.text.toString().trim()
            val passwordStr = password.text.toString().trim()

            if (emailStr.isEmpty() || passwordStr.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            } else {
                // Authenticate with Firebase
                auth.signInWithEmailAndPassword(emailStr, passwordStr)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val currentUser = auth.currentUser
                            currentUser?.let {
                                // User authenticated successfully, check their role
                                checkUserRole(it.uid)
                            }
                        } else {
                            // If sign-in fails, display a message to the user
                            Toast.makeText(baseContext, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        registerBtn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun checkUserRole(userId: String) {
        // Get the user's role from Firebase Database
        database.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val role = snapshot.child("role").getValue(Int::class.java) ?: 0

                    val editor = sharedPreferences.edit()
                    editor.putBoolean("isLoggedIn", true)
                    editor.putInt("role", role)
                    editor.apply()

                    if (role == 1) {
                        Toast.makeText(this@LoginActivity, "Admin Login Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginActivity, AdminHomeActivity::class.java))
                    } else {
                        Toast.makeText(this@LoginActivity, "User Login Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginActivity, UserHomeActivity::class.java))
                    }
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "No role assigned", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LoginActivity, "Failed to retrieve user role", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // login session
    private fun checkUserLoggedIn() {
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            val role = sharedPreferences.getInt("role", 0)
            if (role == 1) {
                startActivity(Intent(this@LoginActivity, AdminHomeActivity::class.java))
            } else {
                startActivity(Intent(this@LoginActivity, UserHomeActivity::class.java))
            }
            finish()
        }
    }
}
